@2019092824 이경준

---
# Index
1. Design
	1. Simplification (introduction)
	2. Plan
2. Implement
3. Result
	1. 빌드 및 실행
	2. 실행 세부 결과
4. Trouble Shooting
---
# Design
본 프로젝트 에서는 크게 RR, FCFS, Priority 세 개념을 이용하여 스케줄링 프로그램을 수정하였다. 명세에서는 크게 네 가지 Multilevel Queue를 구현하도록 되어있고, 각 큐의 특징은 아래와 같다.
- L0
	- 모든 프로세스는 L0 큐에 처음으로 생성된다.
	- Scheduling Type은 RR이다.
	- Time Quantum 은 2 tick 이다.
	- L0 큐의 프로세스가 Time Quantum을 모두 소모했을 경우, 하단 Policy에 의해 큐가 재조정 된다. 이후 Time Quantum은 초기화 된다.
		- PID 가 홀수인 경우 : L1
		- PID 가 짝수인 경우 : L2
- L1
	- 4 Tick 의 Time Quantum을 가졌다.
	- Scheduling Type은 RR이다.
	- L1 큐의 프로세스가 Time Quantum을 모두 소모했을 경우, 하단 Policy에 의해 큐가 재조정 된다. 이후, Time Quantum은 초기화 된다.
		- 모두 L3
- L2
	- 6 Tick의 Time Quantum을 가졌다.
	- Scheduling Type은 RR이다.
	- L2 큐의 프로세스가 Time Quantum을 모두 소모했을 경우, 하단 Policy에 의해 큐가 재조정 된다. 이후, Time Quantum은 초기화 된다.
		- 모두 L3
- L3
	- 8 Tick의 Time Quantum을 가졌다.
	- Scheduling Type은 RR이나, Priority 개념이 추가되었다.
	- L3 큐의 프로세스가 Time Quantum을 모두 소모했을 경우, Demotion은 발생하지 않으며, Priority에 변화를 준 뒤 Time Quantum을 초기화 한다.
- MOQ
	- Scheduling type은 FCFS이다.
	- CPU를 독점하여 사용하는 큐이다.

## 1. Simplification
해당 스케줄러의 매 Tick 마다의 행위를 간단히 표현하면 다음 그림과 같다.
![[Pasted image 20240421003130.png]]
스케줄러의 동작 방식은 다음과 같다.
![[Pasted image 20240421004403.png]]
> ptable 은 프로세스의 정보를 담고 있는 테이블로, 이에 접근할 때는 항상 Lock을 걸고 들어가 나올 때 풀어줘야 한다.
> tick도 마찬가지로 Lock 이 있고, `updatestatistics()` 에서 프로세서의 정보에 접근하기 때문에 역시 ptable에 대한 lock을 걸고, 풀면서 나와야 하지만 삽화의 공간이 작아 표시하지는 않았다.

Monopoly queue의 경우에는 mlfq와 따로 떨어져, CPU를 독점해야 하기 때문에 다르게 구성되어야 한다.
![[Pasted image 20240421005622.png]]

# 2. Plan
본 프로젝트를 수행하기 위해 수정/추가해야 할 것은 다음과 같다.
- param.h
	- `L0TQ~L3TQ` : 각 큐별 time quantum으로, 상수값을 define 해서 가독성 향상
- proc.h
	- 프로세스의 구조체 `proc` 에 변수 추가
		- `tq` : time quantum
		- `mlfqlev` : queue level
		- `ut` : process uptime
		- `priority` : priority
		- `mono` : process가 mono mod인지 아닌지 확인
- System Call
	- 새로운 system call 파일을 만들 필요는 없음. 전부 기존 system call들이 있는 `sysproc.c`에 wrapper 함수를 구현할 것임.
- proc.c에서의 함수 추가 및 수정
	- `scheduler` 함수의 재 구현
	- ptable의 값을 조작하는 함수들 구현
		- `updatestatistics(), setpriority(), setmonopoly(), monopolize(), unmonopoilze() `
- trap.c
	- `trap()` 함수의 수정
- Add new User program
	- `mlfq_test.c` 함수의 구현
---
# Implementation
## param.h
```c
#define L0TQ         2   // timequantum of L0 (multi level feedback queue)
#define L1TQ         4   // timequantum of L1 (multi level feedback queue)
#define L2TQ         6   // timequantum of L2 (multi level feedback queue)
#define L3TQ         8   // timequantum of L3 (multi level feedback queue)

#define LTTQ         20  // timequantum for testing
```
- 작성의 편의성을 위해 큐별 time quantum을 상수값으로 define 해두었다.
## proc.h
```c

// Per-process state
struct proc {
  uint sz;                     // Size of process memory (bytes)
  pde_t* pgdir;                // Page table
  char *kstack;                // Bottom of kernel stack for this process
  enum procstate state;        // Process state
  int pid;                     // Process ID
  struct proc *parent;         // Parent process
  struct trapframe *tf;        // Trap frame for current syscall
  struct context *context;     // swtch() here to run process
  void *chan;                  // If non-zero, sleeping on chan
  int killed;                  // If non-zero, have been killed
  struct file *ofile[NOFILE];  // Open files
  struct inode *cwd;           // Current directory
  char name[16];               // Process name (debugging)

  // custom

  int mlfqlev;                 // 0, 1, 2, 3, 99(MOQ)
  int tq;                      // Process time quantum
  int ut;                      // Process Uptime
							// if(ut == tq), time expired.
  int priority;                // priority value. Start is 0;
  int mono;
};
```
- 프로세스 구조체에 `//custom` 하단부가 새로 추가한 member variable이다.
	- mlfqlev
		- 0,1,2,3,99의 값을 가진다.
	- tq
		- process가 속한 큐의 time quantum이다.
		- `param.h`에서 정의한 timequantum이 할당될 예정이다.
	- ut
		- timer interrupt가 올때마다 1씩 증가한다.
		- 만약 tq == ut이면, 해당 프로세스의 time quantum이 expired 된 것이므로 yield 해야한다.
	- priority
		- priority값은 L3 큐에서만 쓰인다.
		- 시작값은 0이지만, L1, L2에서 L3로 Demotion 될 때 가장 높은 priority 10으로 설정되어 내려온다..
	- Mono
		- 해당 프로세스가 현재 CPU를 독점 사용해야할지 말아야할지 결정한다.
		- 만약 mlfqlev가 99이고, mono가 1이면 현재 monopolize 중이므로 moq에서 스케줄링 해야한다.
## sysproc.c
```c
int
sys_getlev(void){
  return myproc()->mlfqlev;
}

int
sys_setpriority(void){
  int pid;
  int priority;

  if(argint(0, &pid) < 0) return -1;
  if(argint(1, &priority) < 0) return -1;
  return setpriority(pid, priority);
}
int
sys_setmonopoly(void){
  int pid;
  int password;

  if(argint(0, &pid) < 0) return -1;
  if(argint(1, &password) < 0) return -1;
  return setmonopoly(pid, password);
}

int
sys_monopolize(void){
  monopolize();
  return 0;
}

int
sys_unmonopolize(void){
  unmonopolize();
  return 0;
}

```
명세에 기술된대로 구현하였다.
- `argint()` 에 대해
	- setpriority와 setmonopoly 함수는 인자 값을 받아야 한다.
	- 이를 위해 `argint`를 호출하여 인자를 받았다.
## proc.c

```c
int q0 = 0; int q1 = 0; int q2 = 0; int q3 = 0; int qm=0;
int globalticks = 0;
```
- 전역변수들
	- q0~qm
		- 각 큐별 Runnable한 process의 갯수를 저장하기 위한 변수
	- globalticks
		- boost를 위한 변수
		- globalticks == 100 이면 MOQ를 제외한 모든 큐의 프로세스를 L0으로 이동시킨다.

```c
void
scheduler(void)
{
  struct proc *p;
  struct cpu *c = mycpu();
  c->proc = 0;

  for(;;){
    // Enable interrupts on this processor.
    sti();
    // Loop over process table looking for process to run.
    acquire(&ptable.lock);  

    int found = 0;
    //int mono = 0;

    q0 = 0; q1 = 0; q2 = 0; q3 = 0;
    for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
      if(p->state == RUNNABLE){
        if(p->mlfqlev == 0) q0++;
        if(p->mlfqlev == 1) q1++;
        if(p->mlfqlev == 2) q2++;
        if(p->mlfqlev == 3) q3++;
        /*
        if(p->mono == 1){
          mono = 1;

          break;
        }
        */
      }
    }

    // L0 Scheduling
    if(/*mono == 0&&*/  q0 != 0){
      //cprintf("[L0] : %d\n", q0);
      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state != RUNNABLE || p->mlfqlev != 0) continue;
        
        if(p->ut < L0TQ){        
          //cprintf("(L0) proc [%d] in [%d] selected.\n", p->pid, p->mlfqlev);
          found = 1;
          break;
        }
        else{
          if(p->pid % 2 == 0){
            p->mlfqlev = 2;
            p->tq = L2TQ;
            p->ut = 0;
          }
          else{
            p -> mlfqlev = 1;
            p->tq = L1TQ;
            p->ut = 0;
          }
        }
      
      }

      if(found == 1){
        c->proc = p;
        switchuvm(p);
        p->state = RUNNING;
        p->tq = L0TQ;

        swtch(&(c->scheduler), p->context);
        switchkvm();
        c->proc = 0;
      }
    }
    
    // L1 scheduling
    if(/*mono == 0 && */q0 == 0 && q1 != 0){
      //cprintf("[L1] : %d\n", q1);
      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state != RUNNABLE || p->mlfqlev != 1) continue;

        if(p->ut < L1TQ) {
          //cprintf("(L1) proc [%d] in [%d] selected.\n", p->pid, p->mlfqlev);
          found = 1;
          break;
        }
        else{
          p->mlfqlev = 3;
          p->tq = L3TQ;
          p->ut = 0;
          p->priority = 10;
        }
        
      }

      if(found == 1){
        c->proc = p;
        switchuvm(p);
        p->state = RUNNING;
        p->tq = L1TQ;
        swtch(&(c->scheduler), p->context);
        switchkvm();
    
        c->proc = 0;
      }

    }
    
    //L2 scheduling
    if(/*mono == 0 &&*/ q0 == 0 && q1 == 0 && q2 != 0){
      //cprintf("[L2] %d\n",q2);
      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state != RUNNABLE || p->mlfqlev != 2) continue;
  
        if(p->ut < L2TQ) {
          found = 1;
          break;
        }
        else{
          p->mlfqlev = 3;
          p->tq = L3TQ;
          p->ut = 0;
          p->priority = 10; // L1, L2에서 L3로 Demote 될떄 priority 값을 10 (최대값)으로 수정한다.
        }
        
      }

      if(found == 1){
        c->proc = p;
        switchuvm(p);
        p->state = RUNNING;
        p->tq = L2TQ;

        swtch(&(c->scheduler), p->context);
        switchkvm();
    
        c->proc = 0;
      }
    }
    
    // L3 scheduling
    if(/*mono == 0 && */q0 == 0 && q1 == 0 && q2 == 0 && q3 != 0){
      int pmin = 0;
      int maxpid = 0;

      //priority calculating
      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state != RUNNABLE || p->mlfqlev != 3) continue;

        if(p->ut >= L3TQ){
          int curprior = p->priority;
          if(curprior > 0){
            p->priority = curprior-1;
            p->tq = L3TQ;
            p->ut = 0;
          }
        }
      }

      // find most prior process id
      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state != RUNNABLE || p->mlfqlev != 3) continue;

        if(p->priority > pmin){
          pmin = p->priority;
          maxpid = p->pid;
        }
      }      

      // find that process 
      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state != RUNNABLE || p->mlfqlev != 3) continue;

        if(p->pid == maxpid){
          found = 1;
          break;
        }
      }      

      if(found == 1){
        c->proc = p;
        switchuvm(p);
        p->state = RUNNING;
        p->tq = L3TQ;
        swtch(&(c->scheduler), p->context);
        switchkvm();
        c->proc = 0;
      }
    }
  
    if(mono == 1) {
      cprintf("mono changed!\n");
      qm = 0;

      for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
        if(p->state == RUNNABLE){
          if(p->mlfqlev == 99) qm++;
        }
      }
      
      if(qm == 0){
        unmonopolize();
      }

      else{
        struct proc *minP=0;     
        for(p = ptable.proc; p<&ptable.proc[NPROC];p++){     
          if(p->state != RUNNABLE || p->mlfqlev != 99)     
            continue;
          if(minP != 0){    
            if(p->pid < minP->pid)    
              minP=p;
          }   
          else 
            minP = p;
        }
        if(minP != 0){  
          cprintf("mf! : %d\n", minP->pid);
          minP->ut=0;
          c->proc = minP;
          switchuvm(minP);
          minP->state = RUNNING;
          swtch(&(c->scheduler), minP->context);
          switchkvm();
          c->proc = 0;
        }
      }
    }
    
    release(&ptable.lock);
  }
}
```
- 스케줄러 (주석 참고)
	- 스케줄러에 대한 자세한 설명은 아래 Result에서 실행결과와 함께 설명하겠다.

```c
void 
updatestatistics(void) {
  globalticks++;
  struct proc *p;
  acquire(&ptable.lock);
  for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
    switch(p->state) {
      case RUNNING:
        p->ut++;
        break;
      default:
      ;
    }
  }

  if(globalticks == 100){ // boost
    for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
      if(p->state != RUNNABLE) continue;
      if(p->mlfqlev != 99){
        p->mlfqlev = 0;
        p->tq = L0TQ;
        p->ut = 0;
        p->priority = 0;
      }
    }
    globalticks = 0;
    //cprintf("Priority Boosting!\n");
  }
  release(&ptable.lock);
}


int
setpriority(int pid, int priority){
  if(priority < 0 || priority > 10){
    return -2;
  }
  struct proc *p;
  acquire(&ptable.lock);
  for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
    if(p->pid == pid){
      p->priority = priority;
      release(&ptable.lock);
      return 0;
    }
  }
  release(&ptable.lock);
  return -1;
}

int
setmonopoly(int pid, int password){
  
  if(password != 2019092824) return -2;
  if(myproc()->pid == pid) return -4;
  acquire(&ptable.lock);
  struct proc *p;
  int mf = 0;
  for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
    if(p->pid == pid){
      if(p->mlfqlev == 99) //이미 mono임
      {
        release(&ptable.lock);
        return -3;
      }
      else{
        p->mlfqlev = 99;
        mf = 1;
      }
    }
  }
  if(mf == 0) {
    release(&ptable.lock);
    return -1;
  }
  else{
    int monosize = 0;
    for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
      if(p->state == RUNNABLE && p->mlfqlev == 99){
        monosize ++;
      }
    }
    release(&ptable.lock);
    return monosize;
  }
  
  return -5;
}

void
monopolize(){
  
  cprintf("mono!\n");
  acquire(&ptable.lock);
  struct proc *p;
  for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
    if(p-> state == RUNNABLE && p->mlfqlev == 99){
      p->mono = 1;
    }
  }
  release(&ptable.lock);
  
}

void
unmonopolize(){
  
  cprintf("unmono!\n");
  if(holding(&ptable.lock)){ // scheduling 중 발생한 Unmono
    struct proc* p;
    for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
      if(p->state == RUNNABLE && p->mlfqlev == 99 ){
        p->mlfqlev = 0;
        p->tq = L0TQ;
        p->mono = 0;
      }
    }
  }
  else{
    acquire(&ptable.lock);
    struct proc* p;
    for(p = ptable.proc; p < &ptable.proc[NPROC]; p++){
      if(p->state == RUNNABLE && p->mlfqlev == 99 ){
        p->mlfqlev = 0;
        p->tq = L0TQ;
        p->mono = 0;
      }
    }
    release(&ptable.lock);
  }
  
  globalticks = 0;
  
}
```
- 스케줄러 이외에 ptable에 접근하는 함수
	- updatestatistics
		- 매 tick마다 호출되어 ptable 상의 RUNNING process의 ut를 1 중가시킨다.
		- 또한 globalticks도 매 tick마다 하나씩 증가한다.
			- globalticks가 100 이 되었을 때 boosting이 일어나며, boost 이후 globalticks는 초기화 된다.
	- setpriority
		- L3 queue에서 사용하는 Priority 변수를 조정한다.
		- 특정 pid를 가진 process를 ptable에서 찾아 priority 값을 변경해준다.
		- return 값은 명세에 기술된 것과 동일하다.
	- setMonopoly
		- Password를 받아 학번과 일치하면, 해당 pid의 mlfqlev를 99로 만들어주는 함수.
		- return 값은 명세에 기술된 것과 동일하다.
	- monopolize()
		- scheduling을 moq에서만 하도록 하는 함수.
		- monopolize() call이 되면 ptable에서 mlfqlev이 99인 process를 찾게 되는데,
			- 이 프로세스들의 mono 값을 1로 바꿔준다.
		- Scheduler() 에서, mono가 1인 Runnable 한 프로세스가 있을 경우 moq에서만 스케줄링 한다.
	- unmonopolize()
		- unmonopolize()의 경우 크게 두가지 사용 환경이 존재한다.
			- proc.c의 함수로써, scheduler 안에서 사용되는 경우
				- 이 경우 ptable의 lock은 이미 걸려있기 떄문에 ptable의 값에 접근할 때 lock을 걸어주지 않아도 된다.
			- System call로써, User program 안에서 사용되는 경우
				- 이 경우 ptable의 lock을 걸고 값 조정 이후 lock을 풀어줘야 한다.
		- unmonopolize()는 MOQ에 속한 프로세스를 찾아 이들을 L0으로 초기화 하고, mono 값을 0으로 변경해준다.
			- 왜 ut는 초기화 하지 않을까?
				- ut는 moq에서 해당 프로세스가 얼마나 cpu를 점유했는지 알 수 있기 때문에 초기화 하지 않는다.
				- 만약 time quantum이 생긴 시점에서 ut 가 tq보다 크다면, 스케쥴링 과정에서 자연스럽게 L1, L2, L3 큐로 내려가게 될 것이다.
## trap.c
```c
//PAGEBREAK: 41
void
trap(struct trapframe *tf)
{
  if(tf->trapno == T_SYSCALL){
    if(myproc()->killed)
      exit();
    myproc()->tf = tf;
    syscall();
    if(myproc()->killed)
      exit();
    return;
  }

  switch(tf->trapno){
  case T_IRQ0 + IRQ_TIMER:
    if(cpuid() == 0){
      acquire(&tickslock);
      ticks++;
      updatestatistics();
      wakeup(&ticks);
      release(&tickslock);
    }
    lapiceoi();
    break;
// ...

// ...

  if(myproc() && myproc()->state == RUNNING && tf->trapno == T_IRQ0+IRQ_TIMER){
    int timelimit = myproc()->tq;
    int curtime = myproc()->ut;
    int mono = myproc()->mono;
    if(mono){
      // do nothing;
    }
    else{
      if(timelimit > curtime){
        //cprintf("pid [%d] uptime %d/%d\n" ,myproc()->pid, myproc()->ut, myproc()->tq);
      }
      else{
        //procdump();
        yield();
      }
    }
  }

  // Check if the process has been killed since we yielded
  if(myproc() && myproc()->killed && (tf->cs&3) == DPL_USER){
    exit();
  }
```
- 매 tick마다...
	- updatestatistic()을 호출해 프로세스의 정보를 업데이트 한다.
- 이후...
	- 현재 프로세스의 mono 여부를 파악하여, 서로 다른 스케줄링을 진행한다.
		- mono일 경우 :
			- FCFS이기 떄문에 해당 프로세스가 작업을 다 마치고 exit 할때까지 scheduling은 필요없다. 따라서 아무것도 하지 않는다.
		- mono가 아닐 경우
			- 해당 프로세스의 tq와 ut를 비교해, tq를 다 소모했는지 파악한다.
				- tq를 다 소모하지 않았다면, 이 프로세스는 계속 Running하면 되므로 scheduling은 필요없다. 따라서 아무것도 하지 않는다.
				- tq를 다 소모했을 경우, yield하여 scheduling에 들어간다.
## System call 등록
- system call 등록을 위해 수정한 함수는 다음과 같다.
### defs.h
```c
// ...
//PAGEBREAK: 16
// proc.c
int             cpuid(void);
void            exit(void);
int             fork(void);
int             growproc(int);
int             kill(int);
struct cpu*     mycpu(void);
struct proc*    myproc();
void            pinit(void);
void            procdump(void);
void            scheduler(void) __attribute__((noreturn));
void            sched(void);
void            setproc(struct proc*);
void            sleep(void*, struct spinlock*);
void            userinit(void);
int             wait(void);
void            wakeup(void*);
void            yield(void);
void            updatestatistics(void);
int             setpriority(int, int);
int             setmonopoly(int, int);
void            monopolize();
void            unmonopolize();
// ...
```
### syscall.h
```c
// ...
#define SYS_getlev 22
#define SYS_setpriority 23
#define SYS_setmonopoly 24
#define SYS_monopolize 25
#define SYS_unmonopolize 26
```
### syscall.c
```c
// ...
extern int sys_getlev(void);
extern int sys_setpriority(void);
extern int sys_setmonopoly(void);
extern int sys_monopolize(void);
extern int sys_unmonopolize(void);

// ...
// ...

[SYS_getlev]  sys_getlev,
[SYS_setpriority] sys_setpriority,
[SYS_setmonopoly] sys_setmonopoly,
[SYS_monopolize] sys_monopolize,
[SYS_unmonopolize] sys_unmonopolize,

// ...
```
### user.h
```c
// system calls
//...
int getlev(void);
int setpriority(int, int);
int setmonopoly(int, int);
int monopolize(void);
int unmonopolize(void);
//...
```
### usys.S
```asm
//...

SYSCALL(getlev)
SYSCALL(setpriority)
SYSCALL(setmonopoly)
SYSCALL(monopolize)
SYSCALL(unmonopolize)
```
---
# Result
## 빌드 및 실행
```bash
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ make clean
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ make
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ make fs.img
lee@junserver:~/Juns_rep/C++_folder/Hanyang/xv6-public$ qemu-system-i386 -nographic -serial mon:stdio -hdb fs.img xv6.img -smp 1 -m 512
```
## 실행 세부 결과
- test용으로 제공된 `mlfq_test.c` 코드를 실행해, 그 결과를 첨부한다.
```xv6
SeaBIOS (version 1.13.0-1ubuntu1.1)


iPXE (http://ipxe.org) 00:03.0 CA00 PCI2.10 PnP PMM+1FF8CA10+1FECCA10 CA00
                                                                               


Booting from Hard Disk..xv6...
cpu0: starting 0
sb: size 1000 nblocks 941 ninodes 200 nlog 30 logstart 2 inodestart 32 bmap start 58
init: starting sh
$ mlfq_test
MLFQ test start
[Test 1] default
Process 4
L0: 16067
L1: 0
L2: 38945
L3: 44988
MoQ: 0
Process 5
L0: 16533
L1: 34067
L2: 0
L3: 49400
MoQ: 0
Process 8
L0: 17487
L1: 0
L2: 51080
L3: 31433
MoQ: 0
Process 10
L0: 18165
L1: 0
L2: 51808
L3: 30027
MoQ: 0
Process 6
L0: 11699
L1: 0
L2: 38169
L3: 50132
MoQ: 0
Process 7
L0: 15490
L1: 37635
L2: 0
L3: 46875
MoQ: 0
Process 9
L0: 16861
L1: 34362
L2: 0
L3: 48777
MoQ: 0
Process 11
L0: 11560
L1: 23807
L2: 0
L3: 64633
MoQ: 0
[Test 1] finished
[Test 2] priorities
Process 12
L0: 17013
L1: 0
L2: 36560
L3: 46427
MoQ: 0
Process 13
L0: 17288
L1: 35403
L2: 0
L3: 47309
MoQ: 0
Process 15
L0: 17213Process 18
L0: 17225
L1: 0
L2: 52462
L3: 30313
MoQ: 0
Process 14
L0: 11103
L1: 0
L2: 30576
L3: 58321
MoQ: 0

L1: 35642
L2: 0
L3: 47145
MoQ: 0
Process 17
L0: 17964
L1: 35313
L2: 0
L3: 46723
MoQ: 0
Process 16
L0: 10462
L1: 0
L2: 35120
L3: 54418
MoQ: 0
Process 19
L0: 11883
L1: 23708
L2: 0
L3: 64409
MoQ: 0
[Test 2] finished
[Test 3] sleep
Process 20
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 21
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 22
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 23
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 24
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 25
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 26
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
Process 27
L0: 50
L1: 0
L2: 0
L3: 0
MoQ: 0
[Test 3] finished
[Test 4] MoQ
Number of processes in MoQ: 1
Number of processes in MoQ: 2
Number of processes in MoQ: 3
Number of processes in MoQ: 4
monocall!!
Process 29
L0: 0
L1: 0
L2: 0
L3: 0
MoQ: 100000
Process 31
L0: 0
L1: 0
L2: 0
L3: 0
MoQ: 100000
Process 33
L0: 0
L1: 0
L2: 0
L3: 0
MoQ: 100000
Process 35
L0: 0
L1: 0
L2: 0
L3: 0
MoQ: 100000
Process 28
L0: 13292
L1: 0
L2: 49187
L3: 37521
MoQ: 0
Process 30
L0: 15603
L1: 0
L2: 51604
L3: 32793
MoQ: 0
Process 34
L0: 22709
L1: 0
L2: 48559
L3: 28732
MoQ: 0
Process 32
L0: 10356
L1: 0
L2: 32476
L3: 57168
MoQ: 0
[Test 4] finished
```
- Test3의 `NUM_SLEEP` 은 500에서 50으로 임의 조정했다. waiting time이 너무 길어 결과를 보는데 시간이 오래걸렸기 때문이다. 
- Test 4에서, `monopolize()` 시스템 콜을 호출하기 전에 `printf(1, "monocall!"\n)`을 호출했다.
- 먼저 테스트의 목적을 간단히 정리하자면 아래와 같다.
	- TEST 1
		- MLFQ에서, 각 큐간 demotion이 원활하고 Time quantum을 충실히 사용하는가?
	- TEST 2
		- MLFQ의 L3 큐에 접근하는 setpriority 시스템 콜과 그로인해 발생하는 L3 큐의 스케줄링 알고리즘에 문제는 없는가?
	- TEST 3
		- MLFQ의 스케줄링이 Runnable 한 프로세스에 한해서만 발생하는가?
	- TEST 4
		- monopolize() 시스템 콜과, MOQ를 사용한 스케줄링이 원활한가? 또, unmonopolize가 실행되는가?\

이때 실제 프로세스의 스케줄링은 아래 그림으로 정리된다.
![[2024_04_21 오후 2_40 Office Lens.jpg]]

# Trouble Shooting
- xv6에서의 프로세스 스케줄링 구현을 이해하는 데 많은 시간을 들임.
	- 스케줄링과 관련된 모든 줄마다 cprintf를 찍어서 흐름을 잡았음.
- MLFQ Scheduling과 MOQ 스케줄링의 Integration
	- Mono로 한번 바뀌면 Unmonopolize call이 실행되었음에도 다시 MLFQ scheduling으로 돌아가지 않는 문제가 발생함. 그 이유는 다음과 같았음.
```c
void
scheduler(void)
{

  for(;;){
    // Enable interrupts on this processor.
    sti();
    // Loop over process table looking for process to run.
    acquire(&ptable.lock);  
    q0 = 0; q1 = 0; q2 = 0; q3 = 0;
    // count q0~ q3, if there is mono, then break.

    // L0 Scheduling
    if(mono == 0 && q0 != 0) // Do something
    
    // L1 scheduling
    if(mono == 0&& q0 == 0 && q1 != 0) // Do something
    
    //L2 scheduling
    if(mono == 0&&  q0 == 0 && q1 == 0 && q2 != 0) // Do something
    
    // L3 scheduling
    if(mono == 0 && q0 == 0 && q1 == 0 && q2 == 0 && q3 != 0) // Do something
       
    if(mono == 1) {
      // count qm
      if(qm == 0){
        cprintf("unmono!\n");
        unmonopolize();
      }
      else // Do something
      }
    }
    
    release(&ptable.lock);
  }
```
초기 코드의 모습이었다. 큐를 선택하기 전, Mono 상태임이 확인되면 L0~L3 스케줄링을 일단 무시하고, Unmonopolize를 한 다음, 다음 Loop에서 MLFQ scheduling으로 돌아오려 했다.
그러나 "스케줄러 안에서 mono 값을 변경함"을 간과하고 있었다. 
```
mono 0임 -> 어라 mono 1이네? mlfq 안해야겠다
-> 뭐야 실행가능한 프로세스가 없잖아? -> 사실 mono는 0이었어
(이미 mlfq를 배제 했기 때문에 이러지도 저러지도 못하는 상황)
```
이후, 코드를 이렇게 바꿨다.
```c
void
scheduler(void)
{

  for(;;){
    // Enable interrupts on this processor.
    sti();
    // Loop over process table looking for process to run.
    acquire(&ptable.lock);  
    q0 = 0; q1 = 0; q2 = 0; q3 = 0;
    // count q0~ q3, if there is mono, then break.

    // L0 Scheduling
    if(/*mono == 0 &&*/ q0 != 0) // Do something
    
    // L1 scheduling
    if(/*mono == 0 && */ q0 == 0 && q1 != 0) // Do something
    
    //L2 scheduling
    if(/*mono == 0 && */ q0 == 0 && q1 == 0 && q2 != 0) // Do something
    
    // L3 scheduling
    if(/*mono == 0  &&*/q0 == 0 && q1 == 0 && q2 == 0 && q3 != 0) // Do something
       
    if(mono == 1) {
      // count qm
      if(qm == 0){
        cprintf("unmono!\n");
        unmonopolize();
      }
      else // Do something
      }
    }
    
    release(&ptable.lock);
  }
```
이렇게 mono를 배제 하는 파트를 삭제하니, moq에서 unmonopolized 된 후 바로 mlfq context로 돌아올 수 있었다.
