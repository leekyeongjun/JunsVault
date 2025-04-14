
@2019092824 이경준

---
# Index
1. Design
	1. Introduction
	2. Plan
		1. User program `project01`
		2. System Call `sys_getgpid()`
2. Implement
	1. `getgpid` System Call 추가
		1. `sysproc.c`에 `sys_getgpid()`wrapper 함수 추가
		2. System call 등록 및 Macro 설정
	2. `project01.c`에서 system call 호출
3. Result
	1. 빌드 및 실행
	2. 실행 결과
4. Trouble Shooting
	1. Possible Issues
---
## Design
### Introduction
본 프로젝트의 요구 조건은 다음과 같다.
1. 프로젝트 이름을 "project01"으로 할 것.
2. xv6 환경에서 유저 프로그램 `project01`을 실행시켰을 경우 아래 세 가지를 출력할 것
	1. 학번
	2. 프로세스 ID
	3. 조부모 프로세스의 ID

출력 결과의 예시 :
```xv6
Booting from Hard Disk..xv6...
cpu0: starting 0sb: size 1000 nblocks941 ninodes200 nlog30 logstart2 inodestart32 bmapstart 58
init: starting sh
$ project01
My student id is 2022123123
My pid is 3
My gpid is 1
$
```
### Plan
#### User Program `project01`
Introduction의 요구를 충족하는 유저 프로그램을 만들기 위해 구현해야 할 각 요소에 대한 구현 계획을 서술한다.
1. 학번
고정된 값인 "2019092824"가 있으므로 System Call을 이용하지 않아도 된다.
2. pid
xv6의 내장 System Call에 현재 프로세스의 pid를 return 하는 wrapper 함수 `sys_getpid()`가 존재한다. 이를 그대로 사용한다.
- sysproc.c
```C
int
sys_getpid(void){
  return myproc()->pid;
}
```
- syscall.c
```C
//...
[SYS_dup]     sys_dup,
[SYS_getpid]  sys_getpid,
[SYS_sbrk]    sys_sbrk,
//...
```
- syscall.h
```C
//...
#define SYS_dup    10
#define SYS_getpid 11
#define SYS_sbrk   12
//...
```
Assembly 상에서 `sys_getpid`를 사용하기 위한 매크로가 `getpid`임을 확인했다.
- usys.S
```S
;...
SYSCALL(dup)
SYSCALL(getpid)
SYSCALL(sbrk)
;...
```

3. gpid
> gpid는 현재 프로세스의 -> 부모의 -> 부모의 pid를 의미한다.

gpid의 경우 xv6의 내장 system call에 구현되어 있지 않아 별도로 구현해야 한다. 

#### System Call `sys_getgpid()`
현재 프로세스의 조부모 프로세스 id를 return 하는 함수를 `sys_getgpid()`로 구현해야 한다.  
`sys_getgpid()` 의 구현은 xv6의 "프로세스"에 대한 정의를 담고 있는 헤더 파일인 `proc.h`에서 힌트를 얻을 수 있다.
- proc.h
```h
// Per-process state

struct proc {
  uint sz;                     // Size of process memory (bytes)
  pde_t* pgdir;                // Page table
  char *kstack;                // Bottom of kernel stack for this process
  enum procstate state;        // Process state
  int pid;                     // Process ID
  struct proc *parent;         // Parent process
  struct trapframe *tf;        // Trap frame for current syscall
  struct context *context;     // swtch() here to run process
  void *chan;                  // If non-zero, sleeping on chan
  int killed;                  // If non-zero, have been killed
  struct file *ofile[NOFILE];  // Open files
  struct inode *cwd;           // Current directory
  char name[16];               // Process name (debugging)

};
```
주목해야 할 것은 `struct proc *parent`로, xv6의 프로세스는 **부모 프로세스를 가리키는 포인터**를 내장하고 있음을 확인할 수 있었다. 
따라서 `sys_getgpid()`가 현재 프로세스의 `parent->parent->pid`에 접근하도록 하여 조부모의 pid에 접근하려 한다.

---
## Implement
### `getgpid` System Call 추가
#### 1. `sysproc.c`에 `sys_getgpid()`wrapper 함수 추가
- sysproc.c
```c
int
sys_getpid(void){
  return myproc()->pid;
}

int
sys_getgpid(void){
  return myproc()->parent->parent->pid;
}
```
`sys_getgpid(void)`를 위 형태와 같이 구현했다. 별도로 pid를 출력하는 함수를 `proc.c`에 구현하지 않고, `sys_getpid(void)`의 함수 형태를 유지하여 `sysproc.c`에 추가했다.

> 여기서 `sys_getpid` 함수가 실은 별도로 정의된 함수가 아닌 `proc.c`의 `myproc()`에 대한 *pid를 return하는 wrapper함수* 로 정의되어 있는 것을 확인할 수 있다.

- proc.c
```C
struct proc*
myproc(void) {
  struct cpu *c;
  struct proc *p;
  pushcli();
  c = mycpu();
  p = c->proc;
  popcli();
  return p; 
  // 현재 프로세스를 struct proc* 의 형태로 return 하고 있다.
}
```
#### 2. System call 등록 및 Macro 설정
wrapper함수를 구현한 뒤 이를 별도의 system call로 등록했다.
##### Skipping Step 1 ~ 4 in Lab02
- Step 1
	- 새로운 함수를 Implement 한 것이 아니기에 skip 한다.
- Step 2
	- Step 1의 Skip으로 인해 생성된 Source file이 없기에 Skip한다.
- Step 3
	- `getgpid()`는 기본적으로 `proc.c`의 `myproc()` 함수에 대한 wrapper 함수로 동작한다. 따라서 따로 declare할 필요가 없다.
	- defs.h
```h
//PAGEBREAK: 16
// proc.c
int             cpuid(void);
void            exit(void);
int             fork(void);
int             growproc(int);
int             kill(int);
struct cpu*     mycpu(void);
struct proc*    myproc();

// 이미 정의되어 있는 모습.
```
- Step 4
	- wrapper 함수는 이미 추가했다. 따라서 Skip 한다.
##### Step 5. Register new System call
- syscall.h
```h
#define SYS_mkdir  20
#define SYS_close  21

#define SYS_getgpid 22
// 새롭게 getgpid를 정의한다.
```
- syscall.c
```C
// ... line 104
extern int sys_write(void);
extern int sys_uptime(void);
extern int sys_getgpid(void);
//... line 128
[SYS_mkdir]   sys_mkdir,
[SYS_close]   sys_close,
[SYS_getgpid] sys_getgpid,
// ... 
```

##### Step 6.  Declare the function in `user.h`
- user.h
```h
// ... line 23
char* sbrk(int);
int sleep(int);
int uptime(void);
int getgpid(void);
// ...
```

##### Step 7. Add a new macro in `usys.S`
- usys.S
```
... line 30
SYSCALL(sleep)
SYSCALL(uptime)
SYSCALL(getgpid)
```

### `project01.c`에서 system call 호출
- project01.c
```C
#include "types.h"
#include "stat.h"
#include "user.h"

int
main(int argc, char* argv[]){

    printf(1,"My student id is 2019092824\n");
    printf(1,"My pid is %d\n", getpid());
    printf(1,"My gpid is %d\n", getgpid());

    exit();
}
```
- Makefile
```makefile
UPROGS=\
    _cat\
    _echo\
    _forktest\
    _grep\
...
    _zombie\
    _project01\
...

EXTRA=\
    mkfs.c ulib.c user.h cat.c echo.c forktest.c grep.c kill.c\
    ln.c ls.c mkdir.c rm.c stressfs.c usertests.c wc.c zombie.c\
    printf.c umalloc.c project01.c\
    README dot-bochsrc *.pl toc.* runoff runoff1 runoff.list\
    .gdbinit.tmpl gdbutil\
```
---
## Result
### 빌드 및 실행
```bash
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ make clean
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ make
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ make fs.img
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ qemu-system-i386 -nographic -serial mon:stdio -hdb fs.img xv6.img -smp 1 -m 512
```
### 실행 결과
![[Pasted image 20240326220006.png]]
```xv6
SeaBIOS (version 1.13.0-1ubuntu1.1)

iPXE (http://ipxe.org) 00:03.0 CA00 PCI2.10 PnP PMM+1FF8CA10+1FECCA10 CA00


Booting from Hard Disk..xv6...
cpu0: starting 0
sb: size 1000 nblocks 941 ninodes 200 nlog 30 logstart 2 inodestart 32 bmap start 58
init: starting sh
$ project01
My student id is 2019092824
My pid is 3
My gpid is 1
$ 
```
---
## Troubleshooting
### Possible Issues
##### `sys_getgpid()`의 구현에 대하여
- sysproc.c
```C
int
sys_getgpid(void){
  return myproc()->parent->parent->pid;
}
```
1. 만약 현재 프로세스의 조부모 프로세스가 존재하지 않는 경우는 어떨까?
	- 위 함수는 현재 프로세스의 부모의 부모를 포인터를 통해 접근하고 있다. 이를 구현하며 "만약 현재 실행한 프로세스가 조부모 프로세스를 갖지 않는 경우에 대한 예외 처리는 필요없을까?" 고민했다.
	- 다만 이는 불가능할 것이라는 결론에 이르렀다.
		- `procdump()`를 통해 xv6가 시작하자 마자 어떤 프로세스를 생성하는지 파악해보았다.
		- `init`, `sh`, 그리고 `procdump`함수를 호출한 프로세스를 확인할 수 있었다.
			- 즉, xv6는 시작하자 마자 `init`, `sh` 프로세스를 생성하게 되고, `exec`을 통해 `sh` (a.k.a shell) 위에서 프로세스를 생성하기 때문에, 아무리 프로세스를 빠르게 만들어도 init과 sh는 존재할 수밖에 없다.
			- 따라서 `myproc()->parent->parent`는 적어도 `init`임이 보장되어있다.
---
