# Index
---
- [Index](#index)
- [Introduction](#introduction)
  - [Purpose](#purpose)
  - [System Spec](#system-spec)
    - [기기 사양](#기기-사양)
    - [xv6 정보](#xv6-정보)
  - [Implementation Schedule](#implementation-schedule)
- [Design](#design)
  - [1. Understandings of Basic RR scheduler in xv6](#1-understandings-of-basic-rr-scheduler-in-xv6)
  - [2. FCFS (First-come-First-served) Scheduler Implementation](#2-fcfs-first-come-first-served-scheduler-implementation)
    - [1. Specification](#1-specification)
    - [2. Algorithm](#2-algorithm)
      - [어떻게 FCFS 스케줄러를 구현할까?](#어떻게-fcfs-스케줄러를-구현할까)
      - [Modifying the Iterator, `p`](#modifying-the-iterator-p)
      - [System call을 통해 특정 프로세스가 `yield` 한 경우, FCFS 스케줄러는 어떻게 동작해야 할까?](#system-call을-통해-특정-프로세스가-yield-한-경우-fcfs-스케줄러는-어떻게-동작해야-할까)
  - [3. MLFQ (Multiple level feedback queue) \& Priority Scheduling](#3-mlfq-multiple-level-feedback-queue--priority-scheduling)
    - [1. Specification](#1-specification-1)
    - [2. Algorithm](#2-algorithm-1)
      - [MLFQ 스케줄러를 어떻게 구현해야 할까?](#mlfq-스케줄러를-어떻게-구현해야-할까)
      - [Multiple iteration - lock problem](#multiple-iteration---lock-problem)
        - [Scheduler가 Context switching 할 process를 선택한 경우](#scheduler가-context-switching-할-process를-선택한-경우)
        - [Scheduler가 Context switching을 할 process를 선택하지 못한 경우](#scheduler가-context-switching을-할-process를-선택하지-못한-경우)
  - [4. Mode Switch between FCFS \& MLFQ Mode](#4-mode-switch-between-fcfs--mlfq-mode)
    - [1. Specification](#1-specification-2)
    - [2. Algorithm](#2-algorithm-2)
      - [FCFS Scheduler와 MLFQ Scheduler의 통합](#fcfs-scheduler와-mlfq-scheduler의-통합)
- [Implementation](#implementation)
  - [1. FCFS Scheduler](#1-fcfs-scheduler)
    - [전역변수 및 함수 설명](#전역변수-및-함수-설명)
      - [`kernel/proc.c`](#kernelprocc)
    - [`scheduler()`](#scheduler)
    - [Related System call](#related-system-call)
      - [`kernel/mysyscall.c`](#kernelmysyscallc)
  - [2. MLFQ Scheduler](#2-mlfq-scheduler)
    - [전역변수 및 함수 설명](#전역변수-및-함수-설명-1)
      - [`kernel/proc.h`](#kernelproch)
      - [`kernel/proc.c`](#kernelprocc-1)
      - [`kernel/trap.c`](#kerneltrapc)
    - [`scheduler()`](#scheduler-1)
    - [Related System call](#related-system-call-1)
      - [`kernel/mysyscall.c`](#kernelmysyscallc-1)
  - [3. Mode change](#3-mode-change)
    - [전역변수 및 함수 설명](#전역변수-및-함수-설명-2)
      - [`kernel/proc.c`](#kernelprocc-2)
    - [`scheduler()`](#scheduler-2)
    - [Related System call](#related-system-call-2)
      - [`kernel/mysyscall.c`](#kernelmysyscallc-2)
- [Results](#results)
- [Troubleshooting](#troubleshooting)
---
# Introduction

## Purpose

> Project01의 목적은 서로 다른 유형의 Scheduler를 구현하고, 이들 Scheduler 간의 전환을 가능하게 하는 것이다. 

이를 위해 xv6의 프로세스 관리방법, Interrupt 처리 방법, 기존 Scheduler의 구현 사항을 숙지해야 하며, 이에 더하여 FCFS, MLFQ Scheduler의 작동 원리를 이해하고 이를 구현할 수 있어야 한다.

## System Spec

### 기기 사양
```
프로세서	12th Gen Intel(R) Core(TM) i7-1260P   2.10 GHz
설치된 RAM	16.0GB(15.7GB 사용 가능)
저장소	477 GB SSD SAMSUNG MZVL2512HCJQ-00BL7
그래픽 카드	Intel(R) UHD Graphics (128 MB)
시스템 종류	64비트 운영 체제, x64 기반 프로세서
```

### xv6 정보
```
RISC-V 버전 xv6
Git classroom에 업로드된 xv6 소프트웨어를 clone 하여 구현하였음.

clone address : ""
```

## Implementation Schedule
![](download.png)

---

# Design
## 1. Understandings of Basic RR scheduler in xv6

기존 xv6의 scheduler는 다음과 같이 동작한다.
```c
for(;;){
	for(p = proc; p < &proc[NPROC]; p++) {
      acquire(&p->lock);
      if(p->state == RUNNABLE) {
        p->state = RUNNING;
        c->proc = p;
        swtch(&c->context, &p->context);
		// 다음 sched() 호출 시 여기부터 시작
        c->proc = 0;
        found = 1;
      }
      release(&p->lock);
    }
}
```
`proc.c`의 `scheduler()` 함수는 Timer interrupt로 인해 `yield()`가 호출된 후, `sched()`를 거쳐 실행된다.
이때 `scheduler()` 함수의 program counter는 `swtch()` 가 발생한 바로 다음을 가리킨다.

주목해야 할 것은 `for loop`의 iteration 방법인데, `proc` Array를 Iteration 하는 포인터 `p` 가 증가하는 시점,
즉 `p++` 가 행해지는 시점을 명확히 할 필요가 있다. 아래 그림을 보자.

`select` 변수는 다음에 스케줄링 할 프로세스를 가리키는 포인터 (`struct proc *`)이다.

![](Pasted%20image%2020250412191153.png)

여기서 확인할 수 있듯이, 만약 `for loop` 도중 `swtch` 가 호출되어 context switch 가 발생하면, `scheduler`내의 iterator `p` 값은 선택된 프로세스에 **정지한다**. 이는 다시금 `yield`가 호출되어 context switch 가 발생, `scheduler`로 복귀했을 때 증가하게 된다. 즉, process가 선택된 다음 Round에, 전 Round에 선택되었던 process는 후순위로 밀린다는 것.

따라서, 자연스럽게 기존 xv6의 `scheduler`는 process의 생성 순서대로 **순차적으로** 스케줄링되는 Round Robin의 형태를 갖게 된다.

## 2. FCFS (First-come-First-served) Scheduler Implementation

### 1. Specification
- 기존 Scheduler의 변형일 것.
- PCB에 어떠한 조작도 가하지 않을 것. (변수 추가 등)
- 생성 순서대로 프로세스를 선택할 것.
- 프로세스가 선택되면, `TERMINATE` 되거나 스스로 `yield` 하지 않는 한 계속 프로세스 실행 권한을 유지할 것.
- 커널 주요 부분을 수정할 것.

### 2. Algorithm

#### 어떻게 FCFS 스케줄러를 구현할까?
FCFS 스케줄링을 실현할 수 있는 방법은 크게 두가지가 있다.
1. `proc.c`의 `scheduler` 내부에서 FCFS 처럼 동작하도록 하는 것.
2. 타이머 인터럽트를 꺼버리는 것.

![](Pasted%20image%2020250412191321.png)

위 삽화는 각각 1번과 2번 방법을 도식화 한 것이다.
이 중 *1번 방법* 을 선택하였으며, 근거는 아래와 같다.

- 타이머 인터럽트를 끄면 FCFS 스케줄링을 간단히 구현할 수 있다.
	- 매 Tick 마다 `yield()`가 실행되지 않기 때문이다.
- 그러나 이 방법을 사용해 FCFS 스케줄링을 하게 되면 MLFQ 스케줄러와의 **모드 전환이 어려워 진다.**
	- 예컨대 Process 4와 Process 5가 각각 있다고 하자.
		- P4는 P5의 부모 프로세스다. 즉 `proc` 배열 기준 P4의 index가 P5보다 먼저다.
			- MLFQ로 스케줄링 하는 상황, **P5 프로세스 실행 중**에 모드가 **FCFS로 바뀌면** 그 다음 scheduing round 부터는 정상적인 FCFS 스케줄러 였을 때 **P4**로 Context switch가 발생해야 한다.
			- 그러나 타이머 인터럽트를 꺼버리면 그냥 P5가 끝날 때 까지 실행하게 되며, 이는 우리가 원하는 바가 아니다.

좋다. 1번 방법을 이용해 FCFS 스케줄러를 구현할 것이다.
어떻게 구현할 수 있을까?

앞서 xv6의 원래 scheduler에서의 **Iterator `p`의 이동 조건** 에 조작을 가하면, 간단히 구현할 수 있다.

#### Modifying the Iterator, `p`
![](Pasted%20image%2020250412191344.png)

기존 스케줄러의 스케줄링 방법이다.

Iterator `p`가 `sched()` 직후 context restore 시점에 **무조건 하나 늘어나기 때문에** (다음 process를 가리키기 때문에),
이 상황에서는 FCFS가 성립되지 않는다.

FCFS 가 되려면 아래와 같이 되어야 한다.

![](Pasted%20image%2020250412191406.png)
![](Pasted%20image%2020250412191417.png)


위 Flow를 보자.

context switch 할 프로세스를 선택한 시점에, `Iterator p`를 `proc`으로, 즉 프로세스 테이블 배열의 **첫번째 인덱스**로 초기화하고 있다. 이러면 첫번째 인덱스의 프로세스가 `TERMINATED`, 혹은 `SLEEP`하기 전 까지 `p`는 계속 `RUNNABLE` 한 첫번째 프로세스를 가리키게 될 것이다. FCFS의 결과와 정확히 동일하다.

다만 이렇게 구현할 경우 문제가 생긴다. `yield()`의 호출이 프로세스의 CPU 포기와 무관해진다.
P1이 `yield()`를 해서 CPU를 포기해봤자, 위 scheduling policy 하에선 어짜피 P1이 다시 선택되기 때문이다.

#### System call을 통해 특정 프로세스가 `yield` 한 경우, FCFS 스케줄러는 어떻게 동작해야 할까?
본 문제를 해결하기 위해, 반드시 선행되어야 하는 개념이 있다.

**Timer interrupt**로 인해 호출되는 `yield()`와, **System call**에 의해 호출되는 `yield()`는 **다르게** 취급되어야 한다.
그 이유는 프로세스가 자신의 cpu 점유를 놓고자 하는 "**의도의 유무**"와 크게 관련이 있다.

Timer interrupt로 인해 호출되는 `yield()`는 preemptive 하다. 시간이 되면 반드시 호출되므로 프로세스의 의도와는 관계없이 CPU를 놔야 한다. 기존 코드의 수정으로 `scheduler`를 FCFS로 바꿨으니, 그 프로세스가 `RUNNABLE` 해졌다가 다시 `RUNNING` 상태로 전환되는 것은 변함이 없다. 즉 Timer interrupt에 의한 `yield()`는 별 신경 안써도 된다.

문제는 yield system call에 의한 `yield()`이다. 이 경우 프로세스는 명확히 **CPU를 놓고자 하는 의도**가 있다.
따라서 이를 존중해야 한다.

![](Pasted%20image%2020250412191531.png)

위 삽화에서 볼 수 있듯, scheduler로 도달하게 한 `yield()`가 **timer interrupt** 기반인지, **system call** 기반인지에 따라 스케줄러는 다르게 동작해야 한다.

System call에 의해 특정 프로세스가 `yield()`를 호출할 경우, 그 프로세스의 `pid`를 저장하는 변수를 하나 둠으로써 
이 문제를 해결할 수 있다.

`yieldpid`가 바로 그 변수이다.

![](Pasted%20image%2020250412191540.png)
![](Pasted%20image%2020250412191547.png)

위 그림처럼 P1이 yield system call로 `yield()`를 호출했다면, 그 Round에는 P1을 **배제해야 한다.**
그 이후에 다시 timer interrupt에 의해 `yield()`가 호출됐다면, 그때 다시 P1을 선택하면 된다.

왜냐하면 P1은 `yield`를 한 것이지, `SLEEP` 이나 `TERMINATED` 된 것이 아니기 때문이다.
yield System call이 호출된 시점의 Scheduling에서만 배제하면 된다.

## 3. MLFQ (Multiple level feedback queue) & Priority Scheduling

### 1. Specification

- 각 프로세스는 3개의 Queue(L0, L1, L2)에 속한다.
	- L0 : 1tick의 time quantum
	- L1 : 3tick의 time quantum
	- L2 : 5tick의 time quantum, priority 적용됨.
- 새로운 프로세스 생성시 L0으로 배치된다.
- 각 Queue에서의 RUNNABLE 한 프로세스가 없으면, 하위 큐에서 찾는다.
- 각 Queue에서 time quantum을 전부 소모한 프로세스는 Demotion 한다.
- L2 Queue는 Priority가 적용되며, 높은 Priority를 가진 프로세스가 먼저 실행된다. (3~0 순)
- Global tick이 50이 될 때, Priority Boosting이 발생한다.
	- 모든 프로세스의 Queue를 0으로 초기화하며, Time quantum 역시 초기화 한다.
  
### 2. Algorithm

#### MLFQ 스케줄러를 어떻게 구현해야 할까?

MLFQ는 L0 ~ L3 큐를 순회하며 `RUNNABLE` 한 프로세스를 찾아야 한다.
따라서 한번의 Iteration 만으로 가능했던 FCFS와는 달리 Iteration이 여러번 일어나야 한다.

![](Pasted%20image%2020250412191615.png)

위 삽화는 MLFQ 프로세스의 동작을 도식화한 것이다.
`s`는 `p`를 통한 프로세스 테이블의 탐색이 마무리 된 이후, Context를 switch 할 프로세스를 가리킨다.
`lastpid`는 FCFS의 `yieldpid`와 유사하나, `lastpid`의 경우 yield system call과 timer interrupt로 인해 호출된 `yield()`를 구분하지 않고, `yield`한 프로세스의 pid를 저장하고 있는 변수다. 

크게 3가지 부분을 중점적으로 살펴보자.

1. 프로세스의 PCB에는 어떤 데이터가 추가되어야 하는가?
	- 현재 `queue number`, 현재 사용중인 `Time quantum`, L2 큐 스케줄링을 위한 `priority`가 필요하다.
2. 프로세스의 tq(time quantum)는 **언제 소모되어야 하는가?**
	- Timer interrupt가 실행되어 `yield()`가 호출된 시점에, 프로세스를 `RUNNABLE` 로 만들기 전 소모하면 된다.
3. Iteration은 **몇 번 일어나야 하는가?**
	- Scheduler 내부에서 최소 **1번**, 많으면 **9번** 일어날 수 있다.
		- 프로세스 전체의 갯수가 64개 이하이고, Queue 갯수가 3개, 그 중 Priority를 쓰는 L2 Queue의 경우 Priority 가 0, 1, 2, 3 4개 이므로
			- 1(priority boosting) + 1 (tq 소모가 다 안됐을 경우) + 1 (tq 소모 다 한 프로세스 Demotion) + 1 (L0) + 1(L1) + 4(L2 priority 4개) = 9
			- 최대 576 cycle 이내에 MLFQ 스케줄링은 마무리 된다.
				- 개발 머신이 2.1Ghz frequency를 가지므로, wsl, qemu등의 속도 저해 요소를 배제하고 xv6가 본 머신의 main OS일 경우 100ms 이내에 스케줄링이 끝난다.

위 모형을 바탕으로 MLFQ 스케줄러의 **Pseudo code**는 아래와 같다.

![](Pasted%20image%2020250412191623.png)

- Scheduler가 호출되면, 다음에 선택될 프로세스`select`를 선택하기 위해 Process Table을 여러번 iteration하게 된다.
	- 만일 `yield()`를 호출한 프로세스의 Time quantum이 남아있다면, 그 프로세스를 그대로 선택하면 된다.
		- 따라서 `yield()` 를 호출한 Process의 pid를 Scheduler는 인지하고 있어야 한다. 이를 `lastpid`로 정의하였으며, FCFS의 `yieldpid`와는 다르게 `lastpid`는 System call과 Timer Interrupt를 구분하지 않고 해당 프로세스의 pid를 저장한다.
- 본 project01에서는 MLFQ 스케줄러 하에서 System call에 의해 `yield()`를 호출한 프로세스의 Time quantum 변화에 대해 기술되어 있지 않다.
	- 따라서 MLFQ에서의 yield system call 호출이 일어났을 때에도 일괄적으로 Time quantum을 감소시켰다.
#### Multiple iteration - lock problem
스케줄러 내부에서 여러번 Iteration을 하려면, lock을 **acquire하고, release 하는 시점**에 대한 이해가 필요하다.
우선 iterator `p`가 process table을 iterating 하는 도중, lock을 얻는 이유는 PCB 내의 데이터는 **Critical section**이기 때문으로, 해당 값을 변화시키는 것은 **atomic** 하게 동작해야 하기 때문이다.

따라서 Iteration 종료 이후 `swtch()`를 하려면, 해당 프로세스의 lock이 언제 잠기고 풀리는지 파악할 필요가 있다.
아래 그림은 Iteration 바깥에서 context switch를 하는 중, 해당 프로세스의 lock이 어떻게 다뤄지는지에 대한 과정을 묘사하고 있다.

##### Scheduler가 Context switching 할 process를 선택한 경우

![](Pasted%20image%2020250412191639.png)
![](Pasted%20image%2020250412191702.png)
![](Pasted%20image%2020250412191709.png)

##### Scheduler가 Context switching을 할 process를 선택하지 못한 경우

![](Pasted%20image%2020250412191724.png)
![](Pasted%20image%2020250412191735.png)

위 코드를 바탕으로, Pseudo code의 **multiple iteration**을 실현할 수 있다.

## 4. Mode Switch between FCFS & MLFQ Mode

### 1. Specification

- 부팅 시 초기 스케줄러 모드는 FCFS이다.
	- FCFS 모드 하에서 Priority boosting은 발생하지 않는다.
- 모드간의 변화가 발생하면 Global tick count는 0으로 초기화 된다.
- 동일한 모드로의 변화는 에러 메시지를 출력하며, 아무런 변화가 일어나지 않는다.
- MLFQ -> FCFS (`fcfsmode()`)
	- 모든 프로세스의 Queue, time quantum, Priority를 -1로 초기화한다.
	- 다음부터 프로세스의 Scheduling은 오직 Creation time에 의존한다(FCFS).
- FCFS -> MLFQ (`mlfqmode()`)
	- 모든 프로세스는 L0 Queue로, Priority는 1로 초기화한다.
	- 다음부터 프로세스의 Scheduling은 MLFQ에 따른다.

### 2. Algorithm

#### FCFS Scheduler와 MLFQ Scheduler의 통합

![](Pasted%20image%2020250412201321.png)

위 다이어그램은 FCFS와 MLFQ 스케줄러를 통합한 것이다. 몇가지 살펴볼 점이 있다.

- **Previous mode는 왜 검사하는가?**
	- FCFS Scheduler는 for loop 내부에서 선택될 프로세스를 정하고, `swtch()`까지 진행한다.
	- 따라서 다음 Scheduling round 가 도달하기 전 모드가 MLFQ로 바뀌더라도, 여전히 Scheduler의 context는 **FCFS의 loop**를 도는 상태로 복귀한다.
		- 이를 예방하기 위해 FCFS에서 MLFQ로 모드 변환이 발생한 경우 의도적으로 FCFS의 loop를 깨고, 다시 Scheduling Mode를 검사하는 절차가 필요하다.

![](Pasted%20image%2020250412202444.png)

- MLFQ 역시 Scheduling round에 도달하기 전 FCFS로 바뀌었을 경우를 고려해야 하나, MLFQ의 경우에는 `swtch()`가 loop 바깥에 있기 때문에 명시적으로 loop를 깨라고 지시할 필요가 없다.
	- MLFQ의 경우에는 `swtch()`가 발생하면 MLFQ 분기를 빠져나오며, 다시 검사가 가능하다.

![](Pasted%20image%2020250412202650.png)

# Implementation

## 1. FCFS Scheduler

### 전역변수 및 함수 설명

#### `kernel/proc.c`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

// If yield is called by syscall, this is the pid of the process
int yieldpid = 0;
struct spinlock yieldpid_lock;

// ==================================================================
// 기존 코드 생략..
// ==================================================================

// initialize the proc table.
void
procinit(void)
{
  struct proc *p;
  
  initlock(&pid_lock, "nextpid");
  initlock(&wait_lock, "wait_lock");
  initlock(&schedmode_lock, "schedmode_lock");
  initlock(&yieldpid_lock, "yieldpid_lock");
  initlock(&lastpid_lock, "lastpid_lock");

  for(p = proc; p < &proc[NPROC]; p++) {
      initlock(&p->lock, "proc");
      p->state = UNUSED;
      p->qnum = FCFSMODE;
      p->tq = FCFSMODE;
      p->priority = FCFSMODE;
      p->kstack = KSTACK((int) (p - proc));
  }
}

// ==================================================================
// 기존 코드 생략..
// ==================================================================

// 0 : nothing yielded voluntarily 
// else : pid of the process that yielded on a syscall
int
yieldp(void) 
{
  int pid;
  acquire(&yieldpid_lock);
  pid = yieldpid;
  release(&yieldpid_lock);
  return pid;
}

// 0 : default(no yield)
// else : pid of the process that yielded on a syscall
int
setyieldpid(int pid) 
{
  acquire(&yieldpid_lock);
  yieldpid = pid;
  release(&yieldpid_lock);
  return yieldpid;
}
```

- Variables

| Type              | Name             | 역할                                                | 사용       |
| ----------------- | ---------------- | ------------------------------------------------- | -------- |
| `int`             | `yieldpid`       | FCFS 모드 중, System call로 yield한 프로세스의 pid를 저장하는 함수 | `proc.c` |
| `struct spinlock` | `yieldpid_lock`  | `yieldpid`는 한번에 하나의 프로세스만이 설정해야 하므로, 이를 위한 lock   | `proc.c` |

- Functions

| Return value | Name(args)             | 역할                                                                                                                                                             | 사용            |
| ------------ | ---------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------- |
| `int`        | `yieldp(void)`         | Scheduling round에서 혹시 System call에 의한 yielded process가 있는지 확인하는 함수.<br>0 : 해당 라운드에 yield Syscall로 yield한 프로세스 없음.<br>그 외 값 : yield Syscall로 yield한 프로세스의 `pid` | `proc.c`      |
| `int`        | `setyieldpid(int pid)` | Syscall에 의한 yield일 경우, 이 함수를 호출하여 `yieldpid`를 Set                                                                                                              | `mysyscall.c` |

### `scheduler()`
```c
void
scheduler(void)
{
  struct proc *p;
  struct cpu *c = mycpu();

  // FCFS only
  struct proc *fcfs_select = 0;
  int fcfs_selected = 0;

  c->proc = 0;
  for(;;){

    intr_on();
    int found = 0;
    for(p = proc; p < &proc[NPROC];) {
      acquire(&p->lock);
      if(p->state == RUNNABLE) {
	    if(yieldp() == 0){ // if nothing voluntarily yielded
		  fcfs_select = p;
		  fcfs_select -> state = RUNNING;
		  c -> proc = fcfs_select;
		  fcfs_selected = 1;
		  swtch(&c->context, &fcfs_select->context);
        }
        else{ // if something voluntarily yielded, you have to respect that intention
          if(p->pid != yieldp()){
            fcfs_select = p;
            fcfs_select -> state = RUNNING;
            c -> proc = fcfs_select;
	        fcfs_selected = 1;
            setyieldpid(0); // We handled this, so reset it
            swtch(&c->context, &fcfs_select->context);
          }
        }
        c->proc = 0;
        found = 1;
        release(&p->lock);     
        
        if(fcfs_selected == 1){
          // if we select a process, p should be reset.
          p = proc;
          fcfs_selected = 0;
        }else {
          // if we are not able to find a process, we should move to the next one.
          p++;
        }
    }
    if(found == 0) {
      // nothing to run; stop running on this core until an interrupt.
      intr_on();
      asm volatile("wfi");
    }
}
```

- Local variables

| Type            | Name            | 역할                                                                                          |
| --------------- | --------------- | ------------------------------------------------------------------------------------------- |
| `struct proc *` | `p`             | Process table의 iterator.                                                                    |
| `struct proc *` | `fcfs_select`   | Scheduling round 종료 시점에 `swtch()`할 프로세스를 가리키는 포인터.                                          |
| `int`           | `fcfs_selected` | Iteration 종료 시점에 `fcfs_select`가 정해졌는지 아닌지 여부를 나타내는 변수.<br>0 : 선택되지 않음 (default)<br>1 : 선택 됨 |

- Specification 반영사항

| Specification                                                         | Line Number | Description                                                               |
| --------------------------------------------------------------------- | ----------- | ------------------------------------------------------------------------- |
| 기존 Scheduler의 변형일 것                                                   | 전체          | Timer interrupt를 끄는 방향으로 구현하지 않았음                                         |
| PCB에 어떠한 조작도 가하지 않을 것 (변수 추가 등)                                       | `proc.h`    | FCFS만을 위한 변수 추가는 없었음                                                      |
| 생성 순서대로 프로세스 선택할 것                                                    | 45~52       | Process가 생성 순서대로 프로세스 테이블에 들어온다는 성질을 이용, Iterator를 움직일지 말지 결정하며 스케줄링을 구현함 |
| 프로세스가 선택되면, `TERMINATE` 되거나 스스로 `yield` 하지 않는 한 계속 프로세스 실행 권한을 유지할 것. | 22, 30      | Syscall에 의한 yield를 구별하였음.                                                 |


- Design 반영사항

| Design                                                         | Line Number | Description                                         |
| -------------------------------------------------------------- | ----------- | --------------------------------------------------- |
| 어떻게 FCFS 스케줄러를 구현할까?                                           | 전체          | Scheduler의 변형으로 구현하였음.                              |
| Modifying the iterator, `p`                                    | 45~52       | 다음 프로세스의 선택 여부에 따라 Iterator의 이동 여부가 결정됨.            |
| System call을 통해 특정 프로세스가 `yield` 한 경우, FCFS 스케줄러는 어떻게 동작해야 할까? | 22, 30      | System call을 통해 yield한 프로세스를 scheduling round에서 배제. |

### Related System call

#### `kernel/mysyscall.c`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

void
sys_yield(void){
	setyieldpid(myproc()->pid);
    yield();
    return;
}
```

- Functions

| Return value | Name(args)        | 역할                                                                     | 사용           |
| ------------ | ----------------- | ---------------------------------------------------------------------- | ------------ |
| `void`       | `sys_yield(void)` | `yield()` 의 wrapper function. `setyieldpid()`를 통해 호출한 프로세스의 pid를 저장한다. | User program |


## 2. MLFQ Scheduler

### 전역변수 및 함수 설명

#### `kernel/proc.h`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

// Per-process state
struct proc {

  // ==================================================================
  // 기존 코드 생략..
  // ==================================================================
  
  int tq;                      // Time quantum (1,3,5)
  int qnum;                    // Queue num(0-2)
  int priority;                // Process priority (0-3)
  
  // ==================================================================
  // 기존 코드 생략..
  // ==================================================================
};

#define TQ_Q0 1
#define TQ_Q1 3
#define TQ_Q2 5
#define FCFSMODE -1
#define TIMEQUANTUM(x) ((x) == 0 ? TQ_Q0 : (x) == 1 ? TQ_Q1 : (x) == 2 ? TQ_Q2 : FCFSMODE)
```

- Variables

| 변수 명             | 역할                                                       | 사용                      |
| ---------------- | -------------------------------------------------------- | ----------------------- |
| `int tq`         | MLFQ 에서 프로세스 별 사용한 time quantum 값. 0에서 시작하여 점점 증가함.      | `proc.c`                |
| `int qnum`       | MLFQ 에서 프로세스의 큐 위치, 0, 1, 2는 각각 L0, L1, L2 큐를 의미함.       | `proc.c`, `mysyscall.c` |
| `int priority`   | MLFQ, L2 큐 스케줄링에서 사용할 우선도 값 , 초기값은 3으로 시작함.              | `proc.c`, `mysyscall.c` |
| `TQ_Qn`          | MLFQ에서 N번째 큐의 Time quantum limit 값                       | `proc.c`                |
| `TIMEQUANTUM(x)` | MLFQ에서 `qnum`을 넣으면 해당하는 `TQ_Qn` 값으로 치환됨. FCFS 모드일 경우 -1. | `proc.c`                |

#### `kernel/proc.c`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

// which process was last scheduled
// only used in MLFQ
int lastpid = 0;
struct spinlock lastpid_lock;


// initialize the proc table.
void
procinit(void)
{
  struct proc *p;
  
  initlock(&pid_lock, "nextpid");
  initlock(&wait_lock, "wait_lock");
  initlock(&schedmode_lock, "schedmode_lock");
  initlock(&yieldpid_lock, "yieldpid_lock");
  initlock(&lastpid_lock, "lastpid_lock");

  for(p = proc; p < &proc[NPROC]; p++) {
      initlock(&p->lock, "proc");
      p->state = UNUSED;
      p->qnum = FCFSMODE;
      p->tq = FCFSMODE;
      p->priority = FCFSMODE;
      p->kstack = KSTACK((int) (p - proc));
  }
}

// ==================================================================
// 기존 코드 생략..
// ==================================================================

// Create a new process, copying the parent.
// Sets up child kernel stack to return as if from fork() system call.
int
fork(void)
{
  int i, pid;
  struct proc *np;
  struct proc *p = myproc();

  // ==================================================================
  // 기존 코드 생략..
  // ==================================================================
  
  int mode = schedmode();
  acquire(&np->lock);
  if(mode == 0){
    // FCFS
    np->qnum = FCFSMODE;
    np->tq = FCFSMODE;
    np->priority = FCFSMODE;
  }
  else{
    // MLFQ
    np->qnum = 0;
    np->tq = 0;
    np->priority = 3;
  }
  np->state = RUNNABLE;
  release(&np->lock);

  return pid;
}

// ==================================================================
// 기존 코드 생략..
// ==================================================================

// Give up the CPU for one scheduling round.
void
yield(void)
{
  
  struct proc *p = myproc();
  acquire(&p->lock);
  
  if(schedmode() == 1){
    // MLFQ ONLY
    int nexttq = (p -> tq) + 1;
    if(nexttq >= TIMEQUANTUM(p->qnum)){
      p -> tq = TIMEQUANTUM(p->qnum);
    }else{
      p -> tq = nexttq;
    }
    setlastpid(p->pid);
  }

  p->state = RUNNABLE;
  sched();
  release(&p->lock);
}

// ==================================================================
// 기존 코드 생략..
// ==================================================================

// returns lastpid
int
lastp(void){
  int pid;
  acquire(&lastpid_lock);
  pid = lastpid;
  release(&lastpid_lock);
  return pid;
}

// when yield is called, lastpid stores the pid of the process which called yield
int
setlastpid(int pid) 
{
  acquire(&lastpid_lock);
  lastpid = pid;
  release(&lastpid_lock);
  return lastpid;
}

// demote the process to the next queue
// only used in scheduler() because it assumes that the process is already in the lock
int
demoteproc(struct proc * p){
  if(p->qnum == 0){
    p->qnum = 1;
  }else if(p->qnum == 1){
    p->qnum = 2;
    p->priority = 3;

  }else if(p->qnum == 2){
    p->qnum = 2;
    int newpriority = (p->priority) - 1;

    if (newpriority < 0){
      p->priority = 0;
    }else{
      p->priority = newpriority;
    }
  }else{
    return -1;
  }
  p->tq = 0;
  return 0;
}

int
getlev(struct proc *p){
  int mode = schedmode();
  int lev;
  if(mode == 1){
    // MLFQ
    acquire(&p->lock);
    lev = p->qnum;
    release(&p->lock);
    return lev;
  }

  return 99;
}

int
setpriority(int pid, int np){
  if(np < 0 || np > 3){
    return -2;
    // invalid priority
  }

  struct proc *p;
  for(p = proc; p < &proc[NPROC];){
    acquire(&p->lock);
    if(p->pid == pid){ 
      p->priority = np; 
      release(&p->lock);  
      return 0;
    }
    release(&p->lock);
    p++;
  }

  return -1;
  // not found

}
```

- Variables

| Type              | Name           | 역할                                             | 사용       |
| ----------------- | -------------- | ---------------------------------------------- | -------- |
| `int`             | `lastpid`      | 마지막으로 yield한 pid (Syscall, Timer interrupt 무관) | `proc.c` |
| `struct spinlock` | `lastpid_lock` | `lastpid`의 atomicity를 보장하기 위한 lock             | `proc.c` |

- Functions

| Return value | Name(args)                     | 역할                                                                                                                            | 사용                     |
| ------------ | ------------------------------ | ----------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| `void`       | `procinit(void)`               | 프로세스 초기화 함수. 각종 lock 및 초기 프로세스의 PCB 구성요소를 초기화한다.                                                                              | `proc.c`               |
| `int`        | `fork(void)`                   | 새로운 프로세스를 만들 때 호출되는 함수. 모드에 따라 `qnum, tq, priority`를 다르게 설정한다.                                                                | `sysproc.c`            |
| `void`       | `yield(void)`                  | MLFQ 모드일 때, 그 프로세스의 `tq`를 하나 증가시킨 뒤, `lastpid`를 그 프로세스의 `pid`로 설정한다.                                                          | `trap.c`               |
| `int`        | `lastp(void)`                  | `lastpid`를 atomic하게 return 할 때 쓰는 함수.                                                                                         | `proc.c`               |
| `int`        | `setlastpid(int pid)`          | `yield()`가 호출될 때, 호출한 프로세스의 `pid`를 Set하는 함수.                                                                                  | `proc.c`               |
| `int`        | `demoteproc(struct proc *p)`   | MLFQ 스케줄러 내부에서, `tq`를 다 소모한 프로세스를 demote 하는 함수.<br>프로세스의 정보를 변경하는 만큼, 스케줄러 내부에서 lock이 걸려있다고 가정하고 그 안에서만 사용해야한다.               | `proc.c`               |
| `int`        | `getlev(struct proc *p)`       | 프로세스의 `qnum`을 return 하는 system call. FCFS 모드라면 99를 return 한다.                                                                 | `proc.c`,`mysyscall.c` |
| `int`        | `setpriority(int pid, int np)` | 프로세스의 `priority`를 변경하는 system call.<br>Invalid priority : -2<br>not found a process with pid : -1<br>Successful executiom : 0 | `proc.c`,`mysyscall.c` |

#### `kernel/trap.c`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

struct spinlock tickslock;
uint ticks;

// ==================================================================
// 기존 코드 생략..
// ==================================================================

// reset ticks
void
resetticks(void)
{
  acquire(&tickslock);
  ticks = 0;
  release(&tickslock);
}
```

- Variables

| Type              | Name        | 역할                               | 사용                 |
| ----------------- | ----------- | -------------------------------- | ------------------ |
| `struct spinlock` | `tickslock` | `ticks`의 atomicity를 보장하기 위한 lock | `trap.c`           |
| `uint`            | `ticks`     | Global tick                      | `proc.c`, `trap.c` |

- Functions

| Return value | Name(args)         | 역할                       | 사용       |
| ------------ | ------------------ | ------------------------ | -------- |
| `void`       | `resetticks(void)` | Global tick을 0으로 초기화 한다. | `proc.c` |

### `scheduler()`
```c
void
scheduler(void)
{
  struct proc *p;
  struct cpu *c = mycpu();

  // MLFQ only
  struct proc *mlfq_select = 0;
  int mlfq_selected = 0;

  c->proc = 0;
  for(;;){

    intr_on();
    int found = 0;


	// MLFQ
	// if global tick is 50, reset all processes. 
	if(ticks == 50){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE){
		  p->qnum = 0;
		  p->tq = 0;
		  p->priority = 3;
		}
		release(&p->lock);
		p++;
	  }
	  resetticks();
	}

	// Step 1. If yielded process is RUNNABLE and time quantum is not expired, select it.
	for(p = proc; p < &proc[NPROC];) {
	  acquire(&p->lock);
	  if(p->pid == lastp() && p->pid != yieldp() && p->state == RUNNABLE && p->tq < TIMEQUANTUM(p->qnum)){
		mlfq_select = p;
		mlfq_selected = 1;
		break;
	  }

	  release(&p->lock);
	  p++;
	}

	// Step 2. In scheduler, reorder processes.
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->pid == lastp() && p->state == RUNNABLE && p->tq >= TIMEQUANTUM(p->qnum)){
		  // if time quantum is expired, we should demote the process.
		  demoteproc(p);
		}

		release(&p->lock);
		p++;
	  }
	}

	// Step 3. Find a process in L0 queue.
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE && p->qnum == 0 && p->pid != yieldp()) {
		  mlfq_select = p;
		  mlfq_selected = 1;
		  break;
		}

		release(&p->lock);
		p++;
	  }
	}

	// Step 4. If there are no process to run in L0 queue, Find a process in L1 queue.
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE && p->qnum == 1 && p->pid != yieldp()) {
		  mlfq_select = p;
		  mlfq_selected = 1;
		  break;
		}

		release(&p->lock);
		p++;
	  }
	}

	// Step 5. If there are no process to run in L1 queue, Find a process in L2 queue.
	// L2 queue has a scheduling policy of priority_scheduling.
	// Since we only have 4 priority levels and the maximum number of process is 64,
	// we can just brute force it.

	// L2 priority 3
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE && p->qnum == 2 && p->priority == 3 && p->pid != yieldp()) {
		  mlfq_select = p;
		  mlfq_selected = 1;
		  break;
		}

		release(&p->lock);
		p++;
	  }
	}

	// L2 priority 2
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE && p->qnum == 2 && p->priority == 2 && p->pid != yieldp()) {
		  mlfq_select = p;
		  mlfq_selected = 1;
		  break;
		}

		release(&p->lock);
		p++;
	  }
	}
	// L2 priority 1
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE && p->qnum == 2 && p->priority == 1 && p->pid != yieldp()) {
		  mlfq_select = p;
		  mlfq_selected = 1;
		  break;
		}

		release(&p->lock);
		p++;
	  }
	}

	// L2 priority 0
	if(mlfq_selected == 0){
	  for(p = proc; p < &proc[NPROC];) {
		acquire(&p->lock);
		if(p->state == RUNNABLE && p->qnum == 2 && p->priority == 0 && p->pid != yieldp()) {
		  mlfq_select = p;
		  mlfq_selected = 1;
		  break;
		}

		release(&p->lock);
		p++;
	  }
	}     
	
	
	// Step 6. if selected, swtch it.
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
	  // if the mode is changed, we dont need to break the loop.
	  // because mlfq scheduling is done outside of the loop.
	}     
}
if(found == 0) {
  // nothing to run; stop running on this core until an interrupt.
  intr_on();
  asm volatile("wfi");
}

}
```

- Local variables

| Type            | Name            | 역할                                                                                                   |
| --------------- | --------------- | ---------------------------------------------------------------------------------------------------- |
| `struct proc *` | `p`             | Process table의 iterator.                                                                             |
| `struct proc *` | `mlfq_select`   | Scheduling round 종료 시점에 `swtch()`할 프로세스를 가리키는 포인터.                                                   |
| `int`           | `mlfq_selected` | multiple Iteration 종료 시점에 `mlfq_select`가 정해졌는지 아닌지 여부를 나타내는 변수.<br>0 : 선택되지 않음 (default)<br>1 : 선택 됨 |

- Specification 반영사항

| Specification                         | Line Number       | Description                          |
| ------------------------------------- | ----------------- | ------------------------------------ |
| 3개의 queue                             | `proc.h`          | Define을 통해 큐와, 큐의 time quantum을 정의함  |
| 새로운 프로세스 생성시 L0으로 배치                  | `fork()`          | MLFQ 모드에서 프로세스 생성시 L0큐로 설정함          |
| L0 -> L1 -> L2 순의 Search              | 64, 79, 99        | 순차적으로 Process table iteration        |
| Time quantum 전부 소모한 Process의 Demotion | 56                | Time quantum 소모시 `demoteproc` 호출     |
| L2의 Priority Scheduling               | 99, 114, 128, 143 | Priority 높은 순으로 Iteration 배치         |
| Priority Boosting                     | 22                | Global tick이 50이 되면 모든 프로세스를 L0으로 이동 |

- Design 반영사항

| Design                                                    | Line Number                       | Description                                      |
| --------------------------------------------------------- | --------------------------------- | ------------------------------------------------ |
| MLFQ 스케줄러를 어떻게 구현할까?<br>Multiple Iteration - lock problem | 37, 50, 64, 79, 99, 114, 128, 143 | Multiple Iteration과 mlfq_select의 lock을 통해 구현하였음. |

### Related System call

#### `kernel/mysyscall.c`

```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

int
sys_getlev(void){
    struct proc *p = myproc();
    int lev = getlev(p);
    return lev;
}

int
sys_setpriority(void){
    int pid, np;
    argint(0, &pid);
    argint(1, &np);
    
    return setpriority(pid, np);
}

```

- Functions

| Return value | Name(args)              | 역할                                                                                    | 사용           |
| ------------ | ----------------------- | ------------------------------------------------------------------------------------- | ------------ |
| `int`        | `sys_getlev(void)`      | `proc.c`의 `getlev`를 호출하는 Wrapper function                                             | User program |
| `int`        | `sys_setpriority(void)` | User program에서는 `int, int`의 argument를 전달함. <br>이를 받아서 `proc.c`의 `setpriority`함수를 실행함. | User program |

## 3. Mode change

### 전역변수 및 함수 설명

#### `kernel/proc.c`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

// scheduler mode
// 0: FCFS
// 1: MLFQ
int scheduler_mode = 0;
struct spinlock schedmode_lock;



// ==================================================================
// 기존 코드 생략..
// ==================================================================


// 0 : FCFS
// 1 : MLFQ
int
schedmode(void)
{
  int mode;
  acquire(&schedmode_lock);
  mode = scheduler_mode;
  release(&schedmode_lock);
  return mode;
}

// 0 : FCFS
// 1 : MLFQ
int
mlfqmode(void){
  acquire(&schedmode_lock);
  if(scheduler_mode == 1){
    release(&schedmode_lock);
    return -1;
  }

  // FCFS -> MLFQ
  // Move all processes to the first queue
  for(struct proc *p = proc; p <= &proc[NPROC];){
    acquire(&p->lock);
    p->qnum = 0;
    p->tq = 0;
    p->priority = 3;
    release(&p->lock);
    p++;
  }
  scheduler_mode = 1;
  resetticks();
  release(&schedmode_lock);
  return 0;
}

int
fcfsmode(void){
  acquire(&schedmode_lock);
  if(scheduler_mode == 0){
    release(&schedmode_lock);
    return -1;
  }

  // MLFQ -> FCFS
  // Initialize all processes to FCFS
  for(struct proc *p = proc; p <= &proc[NPROC];){
    acquire(&p->lock);
    p->qnum = FCFSMODE;
    p->tq = FCFSMODE;
    p->priority = FCFSMODE;
    release(&p->lock);
    p++;
  }

  scheduler_mode = 0;
  resetticks();
  release(&schedmode_lock);
  return 0;
}

// Deprecated
// 0 : FCFS
// 1 : MLFQ
int
setschedmode(int mode)
{

  acquire(&schedmode_lock);
  if(mode == scheduler_mode){
    release(&schedmode_lock);

    return -1;
  }else{

    if(mode == 0){
      // MLFQ -> FCFS
      // Initialize all processes to FCFS
      for(struct proc *p = proc; p <= &proc[NPROC];){
        acquire(&p->lock);
        p->qnum = FCFSMODE;
        p->tq = FCFSMODE;
        p->priority = FCFSMODE;
        release(&p->lock);
        p++;
      }
    }
    else{
      // FCFS -> MLFQ
      // Move all processes to the first queue
      for(struct proc *p = proc; p <= &proc[NPROC];){
        acquire(&p->lock);
        p->qnum = 0;
        p->tq = TQ_Q0;
        p->priority = 3;
        release(&p->lock);
        p++;
      }
    }

    scheduler_mode = mode;
    resetticks();
    release(&schedmode_lock);
    return 0;
  }
}
```

- Variables

| Type              | Name             | 역할                                                | 사용       |
| ----------------- | ---------------- | ------------------------------------------------- | -------- |
| `int`             | `schedulermode`  | 현재 Scheduler mode를 확인하기 위한 전역 변수                  | `proc.c` |
| `struct spinlock` | `schedmode_lock` | 모드 변경 시 atomicity를 유지하기 위한 lock                   | `proc.c` |

- Functions

| Return value | Name(args)                   | 역할                                                   | 사용            |
| ------------ | ---------------------------- | ---------------------------------------------------- | ------------- |
| `int`        | `schedmode(void)`            | 현재 Scheduler mode를 atomic 하게 return 하는 함수            | `proc.c`      |
| `int`        | `mlfqmode(void)`             | 현재 Scheduler mode를 MLFQ로 만드는 함수                      | `mysyscall.c` |
| `int`        | `fcfsmode(void)`             | 현재 Scheduler mode를 FCFS로 만드는 함수                      | `mysyscall.c` |
| ~~`int`~~    | ~~`setschedmode(int mode)`~~ | 현재 Scheduler mode를 `int mode`에 따라 MLFQ, FCFS로 바꾸는 함수 | *Depracated*  |

### `scheduler()`
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

    if(schedmode() == 0){
      // FCFS
      for(p = proc; p < &proc[NPROC];) {
	    // FCFS Scheduing Loop
        if(schedmode() != 0){
          // if the mode is changed, we should break the loop.
          break;
        }
      }

    else{
        // MLFQ    
    }
    if(found == 0) {
      // nothing to run; stop running on this core until an interrupt.
      intr_on();
      asm volatile("wfi");
    }
  }
}
```
- Specification 반영사항

| Specification                             | Line Number                | Description                                                                                     |
| ----------------------------------------- | -------------------------- | ----------------------------------------------------------------------------------------------- |
| 부팅 시 스케줄러 모드는 FCFS이다                      | `proc.c`                   | `scheduler_mode`를 0으로 초기화 함.                                                                    |
| 모드 전환시 Global tick Reset                  | `mlfqmode()`, `fcfsmode()` | 모드 변경 이후에 `resettick()`호출                                                                       |
| 동일한 모드로의 변화는 에러 메시지 출력 이후 아무런 변화를 일으키지 않음 | `mlfqmode()`, `fcfsmode()` | Wrapper function 단위에서 구현함.                                                                      |
| MLFQ -> FCFS                              | `fcfsmode()`,18            | 모든 프로세스의 Queue, time quantum, Priority를 -1로 초기화한다. 또한 기존 FCFS 스케줄링 루프 안에 있었을 경우 Loop를 Break 한다. |
| FCFS -> MLFQ                              | `mlfqmode()`               | 모든 프로세스는 L0 Queue로, Priority는 1로 초기화한다.                                                         |

- Design 반영사항

| Design                   | Line Number | Description                                          |
| ------------------------ | ----------- | ---------------------------------------------------- |
| FCFS 와 MLFQ 스케줄러의 통합     | 14, 24      | 동일한 Scheduler 함수 내에서 `scheduler_mode`값에 따라 다르게 동작한다. |
| Previous mode의 검사 (FCFS) | 18          | MLFQ에서 FCFS로 모드 변환이 발생한 경우, loop를 break 한다.          |

### Related System call

#### `kernel/mysyscall.c`
```c
// ==================================================================
// 기존 코드 생략..
// ==================================================================

int
sys_fcfsmode(void){
    if(fcfsmode() == -1){
        printf("The mode is already fcfs\n");
        return -1;
    }
    printf("The mode is now fcfs\n");
    yield();
    return 0;
}

int
sys_mlfqmode(void){
    if(mlfqmode() == -1){
        printf("The mode is already mlfq\n");
        return -1;
    }
    printf("The mode is now mlfq\n");
    yield();
    return 0;
}

// ==================================================================
// 기존 코드 생략..
// ==================================================================
```

- Functions

| Return value | Name(args)           | 역할                                                                         | 사용           |
| ------------ | -------------------- | -------------------------------------------------------------------------- | ------------ |
| `int`        | `sys_fcfsmode(void)` | `scheduler_mode`를 0으로 바꾸려고 시도한다.<br>만일 이미 0이라면, 에러메시지를 출력하고 -1을 return 한다. | User program |
| `int`        | `sys_mlfqmode(void)` | `scheduler_mode`를 1로 바꾸려고 시도한다.<br>만일 이미 1이라면, 에러메시지를 출력하고 -1을 return 한다.  | User program |

# Results
# Troubleshooting
