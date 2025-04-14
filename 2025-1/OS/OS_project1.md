# 0405 - practice 01 (yield system call)
초기 접근 방식은 심플했다.
`proc.c`에 `yield()` 가 정의되어있으므로 wrapper function만 만들어서 system call로 등록하면 될 것이다.

## `kernel/mysyscall.c`

```c
#include "types.h"
#include "param.h"
#include "memlayout.h"
#include "riscv.h"
#include "spinlock.h"
#include "proc.h"
#include "defs.h"


void
sys_yield(void){
    yield();
    return;
}
```

system call을 테스트 하기 위한 유저 프로그램도 만들었다.

## `user/p1_test.c`

```c
#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"

int 
main(int argc, char *argv[])
{
    int pid = fork();
    if (pid == 0) {
        for (int i =0; i <= 500; i++) {
            printf("child!\n");
            yield();
        }
    } else {
        for (int i =0; i <= 500; i++) {
            printf("parent!\n");
            yield();
        }
    }
    exit(0);
}
```
- child 와 parent 프로세스 각각 `printf` 실행 후 `yield` system call을 호출해 CPU 점유를 포기하는 방식이다.
- 충분한 숫자로 테스트 해야하므로 500회 출력해보았다.

```
child!
pachild!
rent!
```
- 총 1000회의 출력 중 가끔 이렇게 parent와 child의 출력이 섞이는 일이 발생했다. 두 프로세스가 `yield()`를 호출하기 전, scheduler에 의해 child와 parent process 간 context switching이 일어나 발생하는 문제로 보인다. 어떻게 해결해야 할까?

### `yield` 시스템 콜 호출 전, 두 프로세스 간 Context switching이 일어난 이유는 무엇일까?

- Assume 1 : yield() 가 intr_on()~syscall() 사이에 한번 더 호출됐다 (폐기)

```c
void
usertrap(void)
{
  // ...
  
  if(r_scause() == 8){
    // system call
	// ...
	
    // an interrupt will change sepc, scause, and sstatus,
    // so enable only now that we're done with those registers.
    intr_on();
    syscall();
	}
	
  if(killed(p))
    exit(-1);
  // give up the CPU if this is a timer interrupt.
  if(which_dev == 2)
    yield();

  usertrapret();
}
```
아마 이거 때문인거 같다.
System call을 호출하기 전에 interrupt를 enable 해주는데,
`child trap -> intr_on -> syscall() -> yield()` 하는 과정에서 interrupt가 켜진 시점에 timer interrupt가 들어와서,
Kerneltrap()의 `yield()`가 호출된 것으로 보인다. 
일단 배제.

# 0405 - practice 02 (Debug system call)
조건을 따져보면 아래와 같다.

1. User program이어야 한다.
	- User program이 ticks나 pid, process name 같은 데이터에 접근해선 안된다.
		- 즉 이를 커널에서 대신 처리해 줘야한다.
2. 매 Timer interrupt 마다 같은 프로세스가 선택되어야 한다.
	- User mode에서는 프로세스 스케줄링이 안된다. 그러나 커널에서는 가능하다. `yield()`가 마침 타이머 인터럽트 올때마다 실행된다. 그러면, `yield() -> sched() -> scheduler()` 중 언제 print 해야할까?
		- `scheduler()` 실행 중, 다음에 실행할 프로세스를 선택하고 `swtch()` 하기 바로 직전이다.

```c
for(p = proc; p < &proc[NPROC]; p++) {
      acquire(&p->lock);
      if(p->state == RUNNABLE) {

        p->state = RUNNING;
        c->proc = p;
        // 여기
        swtch(&c->context, &p->context);

        c->proc = 0;
        found = 1;
      }
      release(&p->lock);
    }
```

즉, 유저 프로그램에서는 `fork()` 만 호출해 주면 된다. scheduler 안에서 디버깅용 출력 함수를 호출하면 `scheduler`가 다음 프로세스를 선택할 때마다 출력될것이다.

## `kernel/proc.c`

```c
void
procinfo(struct proc *p)
{
  printf("ticks = %d, pid = %d, name =  %s \n", ticks, p->pid, p->name);
}
```
- proc에서 ticks를 접근하고 있으므로 `kernel/proc.c` 안에 만들었다.
- proc pointer를 인자로 받는 이유는 scheduler 상에서 **다음에 선택될** 프로세스 정보를 얻기 위해서다.
	- `swtch` 가 실행되면 그제야 cpu가 실행하는 process가 바뀌게 되므로, `procinfo`를 호출하는 시점은 아직 context switch 전이다. `myproc()` 등의 함수로 **현재** 실행중인 process 정보를 가져오면 그건 deprecated 된 정보다.
## `user/p2_test.c`

```c
#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"

int 
main(int argc, char *argv[])
{
    int pid = fork();
    if (pid == 0) {
        int ppid = fork();
        if (ppid == 0) {
            for (;;) {}
        } else {
            for (;;) {}
        }
    } else {
        for (;;) {}
    }
    exit(0);
}
```
- 이 유저 프로그램에서는 따로 출력은 하지 않고, fork만 2번 했다. 총 3개의 프로세스가 스케줄링되며 `procinfo`를 호출할 것이다.

실행결과는 아래와 같다.
```
// and so on...
ticks = 314, pid = 4, name =  p2_test
ticks = 315, pid = 5, name =  p2_test
ticks = 316, pid = 6, name =  p2_test
// and so on...
```

# FCFS Blueprint(0406)
FCFS는 Process의 생성 순서대로 Scheduling 해야한다. 먼저 명세를 살펴보면...

1. PCB 내에 변수 추가는 없어야 한다.
2. 생성 순서가 가장 빠른 프로세스를 선택한다.
3. 한번 선택된 프로세스는 자발적인 yield()가 없었다면 끝까지 실행해야한다.
4. 이 스케줄링 매커니즘을 실행하기 위해 중요한 부분을 업데이트 해야한다.

## Ideas
1. `proc.c` 내부의 `scheduler` 함수를 수정해서 FCFS의 효과를 내게 하는 방법
2. `trap` 핸들링 과정에서 FCFS의 원리를 실제로 실현하는 방법

### 만약 첫번째 방법을 쓴다면?

먼저, 현재 xv6에서는 Timer interrupt가 오면 자동적으로 yield()를 하게 되어있기 때문에, RR의 형식을 취하고 있다.
P1, P2, P3 3개의 프로세스가 있다고 할 때,

`P1 running -> timer int -> yield -> scheduler -> proc[NPROC] 순회중 runble 찾기 -> P2 select -> run`
이런 형식으로 돌아가게 된다.

왜냐하면 `scheduler`에서는 p 라는 pointer를 이용해 proc table을 순회하는데, p 가 P1을 가리키는 시점에 swtch 가 발생하고, 다시 yield 가 호출되어 scheduler로 context가 돌아왔을 때 p값이 다음에 가리킬 값은 P2이기 때문이다. 

우리가 FCFS에서 원하는 건 이게 아니다. Scheduling이 호출 될 때, p의 값은 다시 P1을 가리켜야 한다.

`P1 running -> timer int -> yield -> scheduler -> (p 초기화) -> proc[NPROC] 순회중 runble 찾기 `
이렇게 되면 FCFS의 구조에 맞게 된다.

그러면 scheduler 함수의 어디에 조작을 가해야 할까?

```c
// ...
	for(p = proc; p < &proc[NPROC]; p++) {
      acquire(&p->lock);
      if(p->state == RUNNABLE) {
        p->state = RUNNING;
        c->proc = p;
        swtch(&c->context, &p->context);

        c->proc = 0;
        found = 1;
        p = proc; // p의 값을 proc table 초기값으로 초기화
      }
      release(&p->lock);
    }
// ...
```

- 다음 루프로 들어가기 전에 p를 초기화 해주면 될 것 같다. 

FCFS를 테스트 하기 위해, 다음 테스트 파일을 만들었다.
#### fcfs_test.c
```c
#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"

int 
main(int argc, char *argv[])
{
    for (int i = 0; i < 30; i++) {
        int pid = fork();
        if (pid == 0) {
            printf("%d is done\n", getpid());
            exit(0);
        }
    }
    printf("%d is done\n", getpid());
    exit(0);
}```

이 프로그램은 fork()를 29번 호출해서, 각 프로세스가 끝나면 pid is done 을 호출하는 프로그램이다.

결과는 `panic release`

### 2번째 방법을 쓴다면?

yield가 timer interrupt로 인해 발생하는 것이므로,
timer interrupt 가 와도 yield를 안하면 fcfs로 동작한다.

```c
void
usertrap(void)
{
  int which_dev = 0;
  
  // something..
  // give up the CPU if this is a timer interrupt.
  //if(which_dev == 2)
    //yield();

  usertrapret();
}

// ...

void 
kerneltrap()
{
  // ...
  // give up the CPU if this is a timer interrupt.
  //if(which_dev == 2 && myproc() != 0)
  //  yield();

  // the yield() may have caused some traps to occur,
  // so restore trap registers for use by kernelvec.S's sepc instruction.
  w_sepc(sepc);
  w_sstatus(sstatus);
}

```

결과는 성공!

### 이거면 되나?
- 아직 해결하지 못한 문제가 남아있다.
	1. Scheduler 내부에서 FCFS로 동작하도록 바꿔야 나중에 Mode Change 할 때 편할 것으로 보인다.
	2. 첫번째 방법으로는 아직 구현하지 못했다.
	3. 특정 process가 yield 하는 경우는 어떻게 해야할까?
		- yield 테스트 해본 결과 정상적으로 동작한다.

# Switch through xv6's RR and FCFS
- 기존의 RR 스케줄링과, 내가 구현한 FCFS 방식의 스케줄링을 Switch 하려면 어떻게 해야할까?

현재 떠오르는 Idea로는, `trap.c` 내부에 Scheduler의 Mode에 해당하는 변수가 있다면 가능해 보인다.

```c
int sched_mode = 0; // 0: FCFS, 1: RR(XV6 original)

// trap.c ... in usertrap
// give up the CPU if this is a timer interrupt.
if(which_dev == 2 && sched_mode == 1)
	yield();


// trap.c ... in kerneltrap
// give up the CPU if this is a timer interrupt.
 if(which_dev == 2 && myproc() != 0 && sched_mode == 1)
    yield();

```

테스트 해보자. trap.c 에 전역변수로 sched_mode를 할당한다.
결과는 성공적이었다.

그러면 이제 User 단위에서 sched_mode의 값을 변경할 수 있는 System call을 만들어보자.
중요한 것은, sched_mode는 Critical Section에 해당한다는 것이다. 따라서 이 변수를 정할 때에는 반드시
lock을 걸어줘야한다.

`trapinit()`에서, tickslock (타이머 인터럽트가 오면 tick 값을 increment하기 위해 쓰는 lock)
을 init한다. 여기에서 sched_mode lock도 init 해주면 될 것같다.

```c
#include "types.h"
#include "param.h"
#include "memlayout.h"
#include "riscv.h"
#include "spinlock.h"
#include "proc.h"
#include "defs.h"

struct spinlock tickslock;
struct spinlock modelock;

uint ticks;
int sched_mode = 0; // 0: FCFS, 1: RR(XV6 original)
// ...
void
trapinit(void)
{
  initlock(&tickslock, "time");
  initlock(&modelock, "sched_mode");
}
```

이제 sched_mode의 값을 변경해주는 함수를 만들어보자. 

```c
int
setschedmode(int mode) // 0: FCFS, 1: RR
{
  acquire(&modelock);
  if(mode == sched_mode){
    //printf("sched_mode is already %d\n", mode);
    release(&modelock);
    return -1;
  }else{
    sched_mode = mode;
    //printf("sched_mode is set to %d\n", mode);
    release(&modelock);
    return 0;
  }
  
}
```

이제 이 함수를 바탕으로 System call을 만들어보자.
```c
// mysyscall.c

int
sys_fcfsmode(void){
    int mode;
    argint(0, &mode);
    if(setschedmode(mode) == -1){
        printf("The mode is already fcfs\n");
        return -1;
    }
    return 0;
}

int
sys_mlfqmode(void){
    int mode;
    argint(1, &mode);
    if(setschedmode(mode) == -1){
        printf("The mode is already mlfq\n");
        return -1;
    }
    return 0;
}
```
편의상 나중에 mlfqmode로 변환할 거기 때문에 Original RR로 변환하는 System call은 sys_mlfqmode로 만들었다.
System call을 등록한 후, 테스트용 User Program을 만들었다.

```c
#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"


int 
main(int argc, char *argv[])
{
    int pid = fork();
    if (pid == 0) {
        int ppid = fork();
        if (ppid == 0) {
            int proc6_var = 0;
            while (1) { 
                printf("6\n");
                proc6_var++;
                if(proc6_var % 100 == 0){
                    yield();
                }
            }
        } else {

            int proc5_var = 0;
            while (1) {
                printf("5\n");
                proc5_var++;
                if(proc5_var % 100 == 0){
                    fcfsmode();
                }
            }
        }
    } else {
        int proc4_var = 0;
        while (1) {
            printf("4\n");
            proc4_var++;
            if(proc4_var % 100 == 0){
                mlfqmode();
            }
            
        }
    }
    exit(0);
}
```

시작 : fcfs모드
100 사이클 마다 Process 4는 mlfqmode로 전환, process 5는 fcfsmode로 전환, process 6은 yield 하도록 했다.
테스트 해보자 ...
4 -> mode change -> 5 -> 6 

4 -> mode change -> 4 -> 5를 바랬는데, 그게 안됐다.
왜냐하면 한번이라도 yield를 해버리면 p의 값이 다음 proc table로 넘어가 버리기 떄문이다.

즉 trap 단위에서 yield()를 껐다 켰다 하는 건 잘못된 접근인거 같다.
scheduler 내부에서 control 해야한다.
무조건 yield 로 넘어가되, fcfs 모드면 scheduler 내부에서 아무 동작도 하지 않고 원래 run 중이던 프로세스를 다시 run으로 만들어야겠다. 그래야 나중에 모드 변환할 때 큐 간의 이동도 가능해보인다.

# 0408 - Retry FCFS
- 저번 시간에, trap.c에서 yield를 하지 않는 것은 FCFS 를 구현하기 위한 올바른 방법이 아님을 깨달았다.
- 그렇다면 yield -> sched -> scheduler 를 거쳐, Scheduler 내부에서 FCFS를 구현해야한다.
- 그렇다면 어떻게? 우선 schduler의 구조를 보자.
```c
void
scheduler(void)
{
  struct proc *p;
  struct cpu *c = mycpu();

  c->proc = 0;
  for(;;){
    intr_on();

    int found = 0;
    for(p = proc; p < &proc[NPROC]; p++) {
      acquire(&p->lock);
      if(p->state == RUNNABLE) {

        p->state = RUNNING;
        c->proc = p;
        swtch(&c->context, &p->context);

        c->proc = 0;
        found = 1;
      }
      release(&p->lock);
    }
    if(found == 0) {
      intr_on();
      asm volatile("wfi");
    }
  }
}
```

User to User process간 scheduling은 다음 순서로 전개된다.

```
timer int -> usertrap -> yield -> sched -> scheduler

in scheduler...

process : 3, 4, 5 is runble

1st round

p : &proc3
p selected
p++

2nd round

p : &proc4
p selected
p++

3rd round
p : &proc5
p selected
p++
```

FCFS가 되려면...
```
process : 3, 4, 5 is runble

1st round

p : &proc3
p selected
p = proc

p : &proc3
p selected
p = proc

...

```

따라서 for 문의 p++ 를 선택적으로 실행하면 된다.
다음은 implementation이다.

```c
// scheduler mode
// 0: FCFS
// 1: MLFQ

int scheduler_mode = 0;
struct spinlock schedmode_lock;

void
scheduler(void)
{
  struct proc *p;
  struct cpu *c = mycpu();

  struct proc *select = 0;
  int selected = 0;
  c->proc = 0;
  for(;;){
    // The most recent process to run may have had interrupts
    // turned off; enable them to avoid a deadlock if all
    // processes are waiting.
    intr_on();
   
    int found = 0;
    if(schedmode() == 0){
      // FCFS
      for(p = proc; p < &proc[NPROC];) {
        acquire(&p->lock);
        if(p->state == RUNNABLE) {
          select = p;
          
          select -> state = RUNNING;
          c -> proc = select;
          selected = 1;
          swtch(&c->context, &select->context);
        }
        c->proc = 0;
        found = 1;
        release(&p->lock);     

        if(selected){
		  // if something selected, reset the pointer.
          p = proc;
          selected = 0;
        }else{
          // if no runble process, just increment it.
          p++;
        } 
      }
    }
    else{
        // MLFQ
        for(p = proc; p < &proc[NPROC]; p++) {
          acquire(&p->lock);
          if(p->state == RUNNABLE) {
            select = p;
            
            select -> state = RUNNING;
            c -> proc = select;
            selected = 1;
            swtch(&c->context, &select->context);
          }
          // Process is done running for now.
          // It should have changed its p->state before coming back.
          c->proc = 0;
          found = 1;
          release(&p->lock);     
        }
    }
    if(found == 0) {
      // nothing to run; stop running on this core until an interrupt.
      intr_on();
      asm volatile("wfi");
    }
  }
}

int
schedmode(void)
{
  int mode;
  acquire(&schedmode_lock);
  mode = scheduler_mode;
  release(&schedmode_lock);
  return mode;
}

int
setschedmode(int mode)
{
  acquire(&schedmode_lock);
  if(mode == scheduler_mode){
    release(&schedmode_lock);
    return -1;
  }else{
    scheduler_mode = mode;
    release(&schedmode_lock);
    return 0;
  }
}
```

## fcfs_test (0408 ver)

```c
#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"


int 
main(int argc, char *argv[])
{
    int pid = fork();
    if (pid == 0) {
        int ppid = fork();
        if (ppid == 0) {
            int p6 = 0;
            while (1) {
                printf("6\n");
                p6++;
                if(p6 % 1000 == 0){
                    yield();
                }
            }
            printf("6 done\n");
            exit(0);
        }
        else{
            int p5 = 0;
            while (1) {
                printf("5\n");
                p5++;   
                if(p5 % 1000 == 0){
                    yield();
                }
            }
            printf("5 done\n");
            exit(0);
        }

    } else {
        int p4 = 0;
        while (1) {
            printf("4\n");
            p4++;

            if(p4 % 1000 == 0){
                yield();
            }
        }
        printf("4 done\n");
    }
    wait(0);
    exit(0);
}
```

작동 예상

4 -> 5 -> 4 -> 5 -> 4 -> 5 (반복)
6 나오면 실패임.

결과 
4 밖에 안나옴

왜일까? - system call의 yield가 기존 yield의 wrapper function에 불과하기 때문이다.
즉 scheduler는 이 yield가 timer interrupt로 인한 yield인지, system call에 의한 yield인지 모른다.
즉, scheduler에게 이 yield가 syscall에 의한 것인지, timer interrupt 에 의한 것인지 알려줘야 한다.

proc 단위에서 이를 인지하기 위해 yield 한 pid를 저장할 변수가 있어야 함.

# 0409 - Dealing with multiple pointers in proc\[NPROC]

- MLFQ 구현을 위해서는 PROCESS TABLE을 여러번 순회해야 할 필요가 있다.
- 내가 원하는 MLFQ 구조를 구현하기 위해, 기존 For 문 안에서 Switch가 되던 기존 스케줄러의 구조를 바꿔야 한다.

```c
     for(p = proc; p < &proc[NPROC];) {
          acquire(&p->lock);
          if(p->state == RUNNABLE) {
            mlfq_select = p;
            mlfq_selected = 1;
            break;
          }

          release(&p->lock);
          p++;
        }
        
        if(mlfq_selected == 1){
          mlfq_select -> state = RUNNING;
          c -> proc = mlfq_select;
          mlfq_selected = 0;

          swtch(&c->context, &mlfq_select->context);
          c->proc = 0;
          found = 1;
          release(&mlfq_select->lock);
        }

        if(schedmode() != 1){
          // if the mode is changed, we should break the loop.
          // this is the place to initialize a scheduling setup.
        }     
```

- 또 하나, Proc 구조체의 변수를 추가해야 한다.
	- 필요한 변수
		- `int tq` (time quantum)
		- `int qnum` (Queue number)
		- `int priority` (priority)


