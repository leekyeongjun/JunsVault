# 스레드를 어떻게 구현할까?

xv6 상에서, 스레드와 자식 프로세스는 뭐가 달라야 하는가?
대부분 비슷하다. 즉 새로운 프로세스를 만들 때 쓸수 있는 `proc` 구조체를 스레드를 생성하는 데도 사용할 수 있다.
새로운 구조체를 만드는 방법도 있겠지만, proc 구조체를 그대로 갖다 쓰는 것이 더 코드를 짧게 만들수 있다.

# How does fork() work?
Fork는 Parent Process가 Child Process를 만드는 System call이다.
fork는 return을 두 번 하는데, 이는 각각
- 에러가 발생했을 경우 -1
- Parent의 경우 Child의 Pid
- Child의 경우 0

fork는 크게 세 블럭으로 구성된다.
1. allocproc() 
	- proc 구조체를 만든다. 
	- 새로운 PID를 assign한다.
2. Virtual address space를 복사한다.
	- 부모 프로세스의 data pages가 복사된다.
3. Proc 구조체를 초기화한다.
	- sz
	- trapframe
		- User process의 register가 포함되어 있다.
			- 여기에는 Program counter도 있다.
		- %eax를 0으로 설정해 child의 return 값이 0이되도록 한다.
	- Ofile
		- 부모 프로세스가 연 파일은 전부 자녀에게 복사된다.
	- cwd (current working directory)
	- parent
		- 자식 process의 경우 부모 process를 가리키도록 한다.
	- state
		- RUNNABLE로 설정한다.

이후, child의 pid를 return 한다.

```c
int
fork(void)
{
  int i, pid;
  struct proc *np;
  struct proc *curproc = myproc();

  // Allocate process.
  if((np = allocproc()) == 0){
    return -1;
  }

  // Copy process state from proc.
  if((np->pgdir = copyuvm(curproc->pgdir, curproc->sz)) == 0){
    kfree(np->kstack);
    np->kstack = 0;
    np->state = UNUSED;
    return -1;
  }
  np->sz = curproc->sz;
  np->parent = curproc;
  
  // user register를 복사한다.
  *np->tf = *curproc->tf;

  // Clear %eax so that fork returns 0 in the child.
  np->tf->eax = 0;

  for(i = 0; i < NOFILE; i++)
    if(curproc->ofile[i])
      np->ofile[i] = filedup(curproc->ofile[i]);
  np->cwd = idup(curproc->cwd);

  safestrcpy(np->name, curproc->name, sizeof(curproc->name));

  pid = np->pid;

  acquire(&ptable.lock);

  np->state = RUNNABLE;

  release(&ptable.lock);

  return pid;
}
```
- 스레드는 자신만의 스택영역 및 스택 포인터 (esp), 프로그램 카운터 (pc), User process register를 갖는다.
	- 이는 각각 xv6 상에서
		- 스택 포인터 : %esp
			- 스레드 호출할 때 복사되어 스레드 자신만의 값을 가짐
		- 프로그램 카운터 : %eip
			- 스레드 호출할 때 복사되어 스레드 자신만의 값을 가짐
		- 그 외 유저 프로세스 레지스터
			- 일단 프로세스 레지스터 값을 저장할 수 있는 변수를 할당받기 위해 Parent Process에서 복사함.
				- %eax 레지스터의 경우 fork()에서 child process가 정상적으로 생성되었을 때 0을 리턴하기 위해 값을 변경함.
				- (고민되는 부분) thread id를 return 할 수 있지 않을까?
					- 근데 fork 한다고 해서 child가 자신의 pid를 리턴하는 것은 아니니 굳이 eax레지스터에 tid를 저장하지는 않아도 될듯하다.

그렇다면 생각해보자, fork랑 thread를 만드는 함수는 대개 비슷한 형태를 띄어야 한다.
fork가 결국 allocproc을 통해 proc 구조체에 해당하는 메모리를 할당받고, 이걸 부모 프로세스의 데이터를 카피에서 채워넣는 함수에 불과함을 인식하고 스레드를 만드는 함수를 구상해봤다. proc 구조체를 보자.
```c
// Per-process state
struct proc {
  uint sz;                     // Size of process memory (bytes)
  pde_t* pgdir;                // Page table
  char *kstack;                // Bottom of kernel stack for this process
  void *tstack;                // thread stack pointer (for threads)
  int tnum;                    // current thread counter, starts with 0 (for master thread)
  enum procstate state;        // Process state
  int pid;                     // Process ID
  int tid;                     // thread ID
  struct proc *parent;         // Parent process
  struct trapframe *tf;        // Trap frame for current syscall
  struct context *context;     // swtch() here to run process
  void *chan;                  // If non-zero, sleeping on chan
  int killed;                  // If non-zero, have been killed
  struct file *ofile[NOFILE];  // Open files
  struct inode *cwd;           // Current directory
  char name[16];               // Process name (debugging)
};
```
- sz
	- 프로세스 메모리의 사이즈는 parent나 thread나 동일할 것. 따라서 그냥 복사한다.
- pgdir(프로세스 페이지 테이블의 주소) 
```c
  if((np->pgdir = copyuvm(curproc->pgdir, curproc->sz)) == 0){
    kfree(np->kstack);
    np->kstack = 0;
    np->state = UNUSED;
    return -1;
  }
```
이게 fork에서 pgdir를 바탕으로 child process의 페이지 테이블을 만드는 코드인데, `copyuvm`을 통해 복사하는 것을 확인할 수 있다.
그런데, thread같은 경우에는 parent process의 자원을 공유해야한다. 즉 페이지 테이블 영역이 부모의 것과 동일해야 한다는 것. 따라서 fork가 하는 "복사를 통해 새로운 페이지 테이블 할당"은 페이지 테이블을 새로 만드는 것이기 때문에 쓰면 안된다. 그냥 부모의 페이지 테이블 주소와 동일한 주소를 스레드가 가지고 있으면 된다. 이러면 한 프로세스 안에서 스레드간 자원 공유가 가능해진다.

- \*kstack
	- 프로세스별 커널 스택이다. 
![[Pasted image 20240517232342.png]]
- 프로세스별 커널 스택이 따로 존재하는 것을 확인할 수 있다. 스레드에서도 마찬가지로 외부 interrupt, exception, system call 호출을 통해 커널로 진입할 때 커널 스택에 현재 유저 스택의 정보를 저장하고 진입하므로, 커널 스택 역시 필요하다. 따라서 그대로 복사한다.
- \*tstack
	- 이게 통상적인 fork를 통해 child process를 만드는 것과 thread를 통해 스레드를 만드는 것의 가장 큰 차이점이라 할 수 있다.
	- 보통 스레드를 호출할때 함수의 포인터 (void \*)를 같이 넘겨준다. 마치 시그널 처리할때 핸들러 함수의 포인터를 넘겨주는 것 처럼.
		- 즉, 자식 프로세스와 달리 스레드의 경우 호출 직후 새롭게 생성된 프로세스(혹은 스레드)가 instruction을 실행하는 지점이 fork와는 달라져야 한다는 것.
			- (fork의 경우 fork가 실행되고 난 바로 다음 instruction을 실행한다. 이는 parent process와 동일한 지점이다.)
		- 나아가, 부모 프로세스의 모든 유저 스택을 다 복사해서 가져가는 fork와는 다르게 스레드는 실행할 그 함수로 인해 생성되는 stack영역의 정보만 복사해 가면 된다.
		- 따라서, fork와 thread의 가장 큰 차이점은 해당 스택영역을 가리키는 포인터 \*tstack의 값이 NULL이냐 아니냐로 따져볼 수 있겠다.
- tnum
	- 이는 스레드를 호출한 부모 프로세스에서 스레드를 관리하고자 만든 변수다.
		- 프로세스를 생성할 때 pid를 하나씩 늘려 해당하는 값을 자식 프로세스에게 넘겨주는 것처럼, 스레드도 부모 프로세스딴에서 스레드의 총 개수를 인지한 뒤 스레드를 생성할 때 이를 하나씩 늘려 각 스레드에 할당하는 방식을 생각했다.
- Process state
	- RUNNABLE이어야 스케줄링 받을 수 있으므로 RUNNABLE로 설정한다.
- pid, tid
	- (고민되는 부분 2) pid는 부모의 것을 그대로 써야 하나?
		- 스레드의 경우 엄밀히 따지면 새로운 프로세스가 아니니 (물론 proc 구조체를 사용해서 통상적인 process와 별 차이는 없도록 구현했지만) 부모 pid와 같은 pid를 가져도 된다고 생각한다.
		- 그러면 스레드간의 구분은 뭘로 할 것인가?
			- tid라는 새로운 변수를 proc 구조체에 선언한다. 부모 process의 tid는 무조건 0이다.
		- 이 경우, 스레드를 다음과 같은 방식으로 특정할 수 있다.
			- 프로세스 A(pid = 3)와 프로세스 B(pid = 4)에서 각각 스레드를 3개씩 만들었다 치자. 그럼 프로세스 Hierarchy는 다음과 같이 구성된다.
				- A (부모 프로세스) = (pid = 3, tid = 0)
					- tid가 0이므로 A가 부모 프로세스임과 동시에 최상위 스레드임을 알 수 있다.
				- A의 스레드들
					- pid = 3, tid = 1
					- pid = 3, tid = 2
					- pid = 3, tid = 3
				- B의 스레드들
					- pid = 4, tid = 1
					- pid = 4, tid = 2
					- pid = 4, tid = 3
- \*parent
	- fork가 아니니까 스레드의 부모는 부모 프로세스의 부모와 동일하다.
- \*trapframe
	- 스레드 역시 trap이 발생했을 때 현재 프로세스의 진행상태를 저장하고 trap을 handling한다. 따라서 독자적인 값이 필요하다.
		- 일단은 부모 프로세스의 trapframe을 그대로 복사한다.
			- 복사의 의미는 스레드를 만드는 그 시점에만 동일하다는 뜻이지, 새롭게 할당된 스레드의 trapframe은 곧 독자적인 값을 갖게 된다.
- \*context
	- trapframe과 마찬가지로, 스레드만의 context를 할당하는데 의미를 둔다. 따라서 그대로 복사.
```c
struct context {
  uint edi;
  uint esi;
  uint ebx;
  uint ebp;
  uint eip;
};
```
다만 eip와 ebp의 값은 좀 바뀌어야 한다. 스레드가 함수의 포인터를 받기 때문에, 스레드 호출 직후 해당 스레드가 실행할 다음 instruction은 해당 함수 (symbol) 의 첫 instruction 일 것. 따라서 context의 eip 레지스터는 해당 instruction의 주소값을 가리키고 있어야 한다.
그렇다면 ebp는 왜 바꿔야 하나? 이게 base pointer의 역할을 하기 때문, 함수의 시작값 (esp값)을 ebp에 저장해놓고 유지하면, esp값이 암만 변해도 ebp값을 기준으로 복귀 주소에 안전하게 접근할 수 있다. 즉 ebp의 값은 초기 설정된 esp값 (함수의 void \* 가 가리키고 있는 값) 을 그대로 복사해야 한다.
아래의 다른 process 정보들은 그대로 복사한다. 이름 (`name`)의 경우도 부모 프로세스와 동일한 이름을 부여한다.

# 어떻게 Stack 영역을 Initialize 할 것인가?
xv6 공식 문서를 보자.
![[Pasted image 20240518003231.png]]
Figure 2-3 을보면, 초기 stack 영역을 프로세스가 초기화 할 때 다음과 같이 구성한다는 걸 볼 수 있다.
즉, stack 영역을 초기화해서 특정 함수를 실행하도록 만들어야 하는 thread의 경우 해당 코드의 아이디어를 차용할 수 있다.
