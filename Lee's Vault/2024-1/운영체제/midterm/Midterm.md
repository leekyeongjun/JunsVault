---
# Index

---
# Index
---
1. Computer System Overview
2. Operating System Overview
3. Processes and Threads
4. CPU scheduling
5. Process Syncronization
---
# 1. Computer System Overview
## 컴퓨터의 기본 구성 원소
- CPU
- Main Memory
	- 휘발성
- System bus
	- CPU-Memory-I/O module 간의 통신
- I/O modules
	- 2차 저장장치 및 통신 기기 등
---
## 프로세서 (CPU)
### 프로세서 레지스터
- Data I/O를 위한 레지스터
	- MAR (Memory Address)
		- 다음 LOAD/STORE 명령이 적힌 주소를 가리킴
	- MBR (Memory Buffer)
		- STORE, LOAD 될 데이터를 저장함
	- 이외에도 I/O adress, I/O buffer 레지스터가 있음
- Control/Status 레지스터
	- 프로세서의 동작을 컨트롤하기 위한 레지스터
	- 권한 명시
	- 종류
		- PC (프로그램 카운터)
		- IR (명령 레지스터)
		- PSW (프로그램 상태 워드)
			- Condition code
				- P,N,Z,O (condtional operation)에 주로 쓰임
		- Interrupt enable/disable
		- Supervisor/user mode
- User visible 레지스터
	- 사용자 보고 쓰라고 냅둔거

### Instruction Execution
![[Pasted image 20240418130552.png]]
- Fetch
	- IR = PC (PC는 다음에 실행될 Instruction의 주소를 담고있음)
	- PC++
- Execute
	- IR에는 다음 4종류의 Instruction이 들어와 있음
		- P-M : 메모리로부터 CPU로 데이터 이동
		- P-IO : 주변기기로부터 CPU로 데이터 이동
		- D : Data processing (산술, 논리 연산)
		- C : 실행 순서의 변경 (jmp같은거)
![[Pasted image 20240418131524.png]]
## Interrupt/Interrupt handler
- 인터럽트란?
	- Sync/Async 한 event를 process에 알려주는 메커니즘
		- Sync : 내가 발생을 유도한 event 
			- Syscall 등
		- Async : 내가 발생을 유도하지 않은 event
			- 마우스의 움직임 등
	- 대부분이 IO 디바이스에서 나옴
		- IO 디바이스가 CPU보다 훨씬 느리기때문에 CPU가 대기해야하는 상황이 생김
- 인터럽트 핸들러란?
	- 대부분 OS의 일부로, 인터럽트가 들어왔을 때 (대부분 IO가 생성) 이를 처리해주는 서비스 프로그램.

#### Instruction Execution의 변화
![[Pasted image 20240418131830.png]]
- 디바이스 컨트롤러나 시스템 하드웨어가 인터럽트 생성
- 프로세서가 execution 종료 이후, 인터럽트 스테이지로 감
- 프로세서가 인터럽트 감지
- PSW와 PC를 컨트롤 스택에 저장
- PC를 인터럽트값에 의해 로드
- 프로세서의 프로세스 정보를 저장
- 인터럽트 핸들러 호출
- 저장해 뒀던 프로세서의 프로세스 정보를 복구
- PSW와 PC값 복구

> 왜 인터럽트가 필요한가? : CPU를 덜 놀리려고
![[Pasted image 20240418132214.png]]
- 인터럽트가 없으면
	- 1->4->(아직 IO 안끝남. CPU 대기) -> 5 (끝남) -> 2 -> 4 -> (대기) -> 5 (끝남) -> 3
- 인터럽트가 있으면
	- 1->4->2a(아직 인터럽트는 안끝남. IO가 일 끝내면 5에서 인터럽트 쏠거임) -> 5 (끝남) -> 2b -> 4 (마찬가지)
		- 2a만큼 일을 더할 수 있음

> 그럼 문제 해결? No. IO가 생각보다 엄청 길어져서 다음 IO 인터럽트가 올 때 까지 안끝나면?
> -> 여전히 대기 해야함.

![[Pasted image 20240418132545.png]]
- 인터럽트가 있어도
	- 1-> 4 (IO시작)-> 2 -> (대기) 왜 아직도 안끝나... -> 5(끝남) -> 4 (IO 시작) -> 3 -> (대기) 왜 아직도 안끝나... -> 5 

> 이 문제를 해결하기 위해 등장한 개념이 MultiProgramming.

## MultiProgramming
- 프로세서가 굳이 하나의 프로그램만 점유해야 하나?
- IO 오면 핸들러 호출하고 CPU를 놔버려 (그동안 다른거 해)
	- 그럼 핸들러 호출 끝나도 다시 그 프로세스를 실행하지 않을 수도 있음 (스케쥴링에 따라 다름)

---
## 메모리
### 메모리 위계
- 위로 올라갈수록 빨라지고, 크기는 작아지고, 비싸짐
- 아래로 내려갈 수록 싸지고, 크기는 커지고, 느려지며, 프로세스가 자주 접근하지 않음 (CPU에서 멀어짐).
![[Pasted image 20240418133050.png]]
> CPU와 High hierarchy memorry 는 그 속도가 점점 빨라지는 경향을 보이는 반면, Low hierarchy memory는 그 속도의 상승폭이 점점 줄고 있음.
> 이러한 경향에서 등장하는 것이 바로 캐시 메모리.

### Cache memory
- Cache, a gateway of Main Mem and cpu.
- 프로세서와 MM 사이의 작고 빠른 메모리
![[Pasted image 20240418133352.png]]
- Main memory에서 자주 접근하는 데이터는 Cache에 남아 CPU가 빠르게 접근 가능.
- CPU는...
	- 메모리의 데이터가 필요할 때 먼저 Cache를 찾는다.
	- 자주 접근하는 데이터는 Cache에 있기때문에 대부분은 Cache선에서 마무리 된다(Cache hit).
	- 만약 CPU가 원하는 데이터가 Cache에 없으면 (Cache miss), 그제야 메인메모리를 훑는다. 이후 그 데이터는 Cache에 갱신되어 저장된다.

#### Cache design
- 작은 캐시는 성능에 큰 영향을 준다. (작아야 함)
- Block size
	- 캐시는 메인 메모리의 데이터를 블록 단위로 저장해 두는데, 이 블록 사이즈가 커지면 커버하는 데이터 양이 많아져 Cache hit 가 날 확률이 높아진다. 대신 접근속도가 느려진다.
- 즉 Cache는 Small size와 Big block size 사이의 균형잡기.
- Mapping function
	- 블록이 저장될 때 Cache의 어디에 위치 시킬지도 중요하다.
- Replacement algorithm
	- Cache가 Miss 나서 새 데이터를 Cache에 올려야 할 상황이 왔을 때, 어떤 데이터를 갔다 버리고 그자리에 넣을지 생각해야한다.
	- LRU (Least Recently Used) 가장 최근에 덜쓰인애 를 주로 날린다.
- Write policy
	- CPU가 Cache에서 데이터를 받아 이를 변경했을 때, Mem에 이 변동사항을 적용시켜야 한다. 근데 Mem은 느리다.
		- Write through ( 변동사항 생기면 바로 적용 )
		- Write back ( 변동사항 다 끝나면 적용 )
			- 둘다 멀티코어 환경에서 Cache coherence problem이 발생할 수 있다.
#### Disk cache
- 캐시가 CPU와 Mem 사이의 gateway면, Disk cache는 Low hierarchy mem과 High hierarchy mem 간의 gateway다.
- MM의 일부는 디스크의 데이터를 쥐고 있어서, 자주 쓰이는 disk의 데이터를 빠르게 받아올 수 있도록 한다.

### Programmed IO
- IO 디바이스는 각각 버퍼와 컨트롤러를 갖고 있기에, CPU와 동시적으로 일을 처리한다.
- 즉, 프로세서가 직접 읽고 쓰는게 아니라, IO 모듈이 CPU의 요청이 들어오면 일을 시작하는 것.
- 인터럽트에 의해서가 아니라, CPU가 IO status register의 비트를 설정하는 것으로 IO를 시작한다.
	- 프로세서는 이 작업이 끝날때까지 Status를 지속적으로 check 한다.
![[Pasted image 20240418134641.png]]
### Interrupt driven IO
- 인터럽트가 등장하면 얘기는 달라진다.
- IO 요청이 있을 때, CPU는 IO 디바이스가 보낸 인터럽트를 감지한다.
- 인터럽트를 감지하면 CPU는 인터럽트 핸들러를 호출한다.
- 이도 여전히 프로세서의 시간을 많이 잡아먹는다.
- 하지만 어쩔수 없다. IO 디바이스가 직접 메모리에 접근하는건 너무 위험하니까.
![[Pasted image 20240418135016.png]]
### Direct Memory Access (DMA)
- 너무 위험한 그 짓을 누군가 시도했다.
- "인증된" IO 디바이스에 한해서 메모리 직접접근을 허락한 것.
- CPU는 인터럽트만 쏘고 딴거 해도된다. IO 디바이스가 메모리에 직접 접근해서 처리해주고 끝나면 Interrupt쏴줄것.
![[Pasted image 20240418135139.png]]
---
# Operating System Overview
## Operating System Structure
- Multiprogramming
	- Single User는 CPU와 I/O device를 항상 바쁘게 할 수 없다.
	- 이런 남는 시간들을 없애 CPU가 항상 일할 수 있도록 하는게 multiprogramming 이다.
	- CPU가 하는 일은 Job Scheduling으로 선정한다.
- Time Sharing (MultiTasking)은 이런 CPU의 동작이 매우 자주 발생해서 User로 하여금 여러개의 작업을 동시에 실행하는 것 처럼 보이도록 하는 것을 의미한다.
	- 반응 시간은 가급적 빠른게 좋다.
	- 각 User는 적어도 하나의 프로그램은 메모리에 올려 실행한다 -> 이 단위를 프로세스라 한다.
	- 만약 각각의 일들이 동시에 행해질 준비가 되어있다면 (Ready) -> CPU 스케줄링한다.
	- 만약 메모리공간이 부족해 더이상 프로세스를 올릴수 없게 된다면, Swapping 이라는 걸 사용해서 점유하던 메모리를 disk 상으로 내려야 한다.
	- VM은 Swapping을 보다 효율적으로 할 수 있게 해주는데, 이는 프로세스를 메모리에 올릴 때 전체 메모리를 다 올리는게 아니라 필요로 하는 메모리만 올려 메모리를 가볍게 유지한다.
## Operating System Operation
> 한편, OS는 하드웨어 기반의 인터럽트와, 소프트웨어 기반의 exception, trap에도 대응해야 하는데, 이는 정상적인 프로그램 실행을 방해하기 때문이다.

- Asyncronous (비동기적) : interrupt (언제 올지 몰라)
- Syncronous (동기적) : exception(trap, falut, abort)
	- trap : 고의적인 exception
	- fault : 회복 가능한 에러 -> 대응 이후 다시 프로그램을 실행할 수 있다.
	- Abort : 회복 불가능한 에러 -> 프로그램 종료

> 그래서 얘들을 어떻게 처리하는가? 바로 Dual mode 다.

#### Dual mode
애플리케이션은 사용자가 코딩한 것만 실행하지 않는다. OS의 기능을 쓰고 싶으면 System Call을 쓰면 된다. (Interface)
Dual mode란 OS로 하여금 다른 시스템 컴포넌트 들로부터 자신을 보호할 수 있도록 한다.
이를 직접 사용하지 않고 껍데기를 씌워놓은게 라이브러리 함수이다.

- 크게 User 과 Kernel 모드로 나뉜다.
	- 사용자의 코드는 권한이 매우 제한적이다.
	- OS 코드는 권한이 크다. (하드웨어 직접 접근 권한 같이..)
		- 따라서 코드를 실행할 때 권한의 전환(Transition)이 발생하게 된다.
- 이는 하드웨어에 의해 제공된 Mode bit에 의해 결정된다.
	- PSR에 있는 Mode bit이다.
![[Pasted image 20240418151721.png]]
## Process Management
### What is process?
> 프로세스는 자료구조다. 프로그램이 돌아가기 위해 필요한 모든 것을 이 자료구조 안에 담았다.
> 마치 식판같은 거라고 생각하면 된다.
> 커널은 System call을 통해 프로세스 자료구조를 생성하고, 여기에 프로그램이 돌아가기 위한 모든 것을 담는다.

- 사전적 정의
	- 사전적 정의의 프로세스는 "실행중인 프로그램" 이다.
- 프로세스는 작업을 마치기 위해 특정한 리소스가 필요하다.
	- CPU나, Mem, I/O, file등..
	- 초기화 데이터
- 프로세스가 종료되면 이 리소스들을 다 뱉어내야 한다.
- 스레드가 한개인 프로세스는 하나의 PC만을 필요로 하나, 여러개일 경우 스레드 하나당 하나의 PC, 즉 복수의 PC가 필요하다.

### Process Management Activities
- OS는 PM을 위해 아래 5가지의 행동을 한다.
1. process (이하 proc) 의 생성 및 삭제
2. proc의 Suspend, resume
3. proc끼리의 Syncronization
4. proc끼리의 Communication
5. proc끼리의 deadlock handling

## Memory Management
1. 어디서, 누가 메모리의 어떤 파트를 사용하고 있는지 파악
2. 어떤 프로세스에 어떤 데이터를 빼다 줄것인지 결정
3. 메모리의 공간의 할당/할당제거

## Storage Management
- OS는 일정하고 논리적인 정보 저장고로서 행동한다.
- 모호한 Physical Properties를 logical한 Storage unit, "File"로 저장한다.
- 각 자료는 디바이스들에 의해 조정된다.

### File System management
- File은 주로 directories로 조직된다.
- 누가 어떤 파일에 접근할 수 있는지 Access control 한다.
- OS는 이런 것들을 한다.
	1. file, dir의 생성및 삭제
	2. file,dir을 조작하기 위한 primitive 제공
	3. file들을 이차 저장소로 mapping
	4. Stable 한 저장소로 휘발성 있는 메모리들을 백업

### Mass Storage Management
- 많은 양의 데이터는 주로 disk에 저장된다.
- 컴퓨터의 동작은 이 disk와 disk 알고리즘에 의해 좌지우지 된다.
- OS는 이런것들을 한다.
	1. Free Space management
	2. Storage allocation
	3. Disk scheduling
- 몇몇 저장소는 빠를 필요가 없다.
	- 3차 저장장치에는 광학 저장장치, 자기테이프등이 있다.
	- WORM (Write once and Read Many) ~ RW 까지 다양하다
	- 그래도 maniging되어야 한다.

#### From a disk to Register
이렇게 많은 단계를 거쳐 disk에서 cpu로 데이터가 오게 된다.
"Multitasking" 환경에서는 어떤 위계의 저장공간에서 값이 오든지 이 값이 가장 최신의 값임이 보장되어야 한다. 이는 어려운 일이다.
만약 분산컴퓨팅이라면? 컴퓨터 자체가 여러대라 더 복잡해진다.

## IO subsystem
- OS는 어떤 형태의 외부 하드웨어가 들어오든지 그 녀석들의 특성을 숨길 필요가 있다. 사용자가 IO 디바이스의 특징을 크게 구분하지 않아도 사용할 수 있도록 해야한다는 뜻.
- IO 하위시스템은 이런걸 한다.
	1. Mem Management of IO
		1. buffering : 중간단계 저장을 통해 IO를 빠르게 한다.
		2. caching : 자주쓰는 데이터를 디스크에서 미리 받아와 느린 하드디스크 접근을 최소화
		3. spooling : 작업이 생성해 낸 Output을 다른 작업에 보낼 때 직접 보내는게 아니라 중간 단계 (Spool)을 사용해서 거기를 통해 와리가리 하게 함.
---
## Protection and Security
또한 OS는 이 모든 작업을 Protection과 Security를 최대한으로 유지하며 실행해야 한다. 그런데 언뜻 비슷해 보이는 이 두단어, 보호와 보안, 어떤 차이일까?
### Protection (보호)
- 보호는 권한이 없는 자가 권한이 필요한 프로세스나 자원에 접근할때 이를 막아주는 것을 의미한다.
### Security (보안)
- 보안은 시스템을 내/외부적인 공격으로부터 방어하는 것을 의미한다.

> 시스템은 통상적으로 먼저 user를 구별하게 된다.
1. user id
	- 사용자 ID는 액세스 제어를 결정하기 위해 해당 사용자의 모든 파일, 프로세스와 연결된다.
1. group id
	- 그룹 ID를 사용하면 사용자 집합을 정의하고 제어하여 관리한 다음 각 프로세스, 파일과 연결할 수 있다.
2. privilege escalation (권한 승급)
	- 프린트할때 내가 없던 권한을 받아와야만 실행할 수 있을 때가 있음. OS가 관리하는 Spool은 사용자가 아닌 OS의 영역임. 이 영역에 사용자가 접근하는 건 불가능함. 그러나 프린트할 때처럼 예외적으로 권한을 승급해서 Spool에 쓸 수 있게 됨.
---
## Operating System Services
- OS는 곧 Service를 제공한다고 한다. 왜 Service일까? 유저의 필요에 따라 유저에게 도움되는 함수를 제공하기 때문이다. 예컨대...
	- User Interface
		- 유저가 시스템을 사용할 수 있도록 CLI (command line interface)든, GUI (Graphics user interface)든 제공한다.
	- Program execution
		- 프로그램을 메모리에 올리고 실행시킨뒤 정상/혹은 비정상 적으로 종료시켜 줘야 한다.
	- IO operation
		- IO device나 file등에 IO가 필요하면 해줘야 한다.
	- File-System Manipulation
		- 프로그램은 파일과 디렉토리를 읽고 쓰고, 생성 및 삭제하고, 검색하고, 파일 정보를 나열하고, 권한을 관리해야 한다.
	- Communication
		- 프로세스 간 혹은 네트워크를 통한 컴퓨터 간 통신이 가능해야 한다.
	- Error detection
		- 발생가능한/혹은 발생한 에러에 대해 인지해야 하며, 그럼에도 최대한 시스템 자체는 일관되게 동작해야 한다.
	- Resource allocation
		- 여러 user나 proc들이 동시에 일어나고 있을 때, CPU의 자원은 적절히 할당되어야 한다.
	- Accounting
		- 어떤 유저가 컴퓨터의 resource를 얼마나 사용하는지 알아야 한다.
	- Protection and security
		- 시스템 리소스의 접근권한인 Proteciton, 외부 IO 디바이스나 공격으로부터 시스템을 보호하는 보안기능이 있어야 한다.
---
- 위계도
![[Pasted image 20240418155116.png]]
---
## System call
- 어떤 파일을 복붙하는 간단한 시퀀스에도...
![[Pasted image 20240418155222.png]]
> 이렇게나 많은 동작이 필요하다.

유저 권한으로는 할 수 없는 일들도 존재한다. 그럼 커널에 System call을 호출해서 처리해야 된다. System call이란 뭘까?
### Implementation
- SYSCALL 은 특정한 숫자에 배정되어 있다.
	- SYSCALL interface는 이 숫자들과 syscall 함수들을 인덱싱한 테이블이라고 생각하면 된다.
- SYSCALL interface는 OS 커널에서 의도한 SYSCALL을 호출해주고, return value를 return 해준다.
- 당연히 호출한 사람은 SYSCALL이 어떻게 구현되어있는지 알 필요가 없다. SYSCALL interface가 알아서 호출하고, 리턴해주니까.
	- OS에서 제공하는 API (application interface)에 입각해서 호출하고, OS의 동작에 대해 이해하기만 하면 된다.
	- 그래서 대부분의 OS동작은 API에 의해 User한테서 숨겨저 있다.
![[Pasted image 20240418155729.png]]
> 직접호출 시, SYSCALL Interface가 indexing 된 SYSCALL을 실행하고, return 값을 뱉어준다.

- Library
- 똑똑한 사람들이 이런 System call interface의 동작법에 따라, 자주 쓰는 기능들을 한데 묶어 user program으로 만들어 놨다. 한마디로 포장지를 하나 더 씌운 셈.
![[Pasted image 20240418155907.png]]
> 그러니까 우리는 심지어 SYSCALL을 직접 호출할 필요도 없이, Library 함수를 쓰면 우리가 원하는 기능을 아주 잘 구현할 수 있는 거다.

#### SYSCALL의 type과 예시
SYSCALL은 주로 이런 일들을 한다.
- Process control 
- File management 
- Device management 
- Information maintenance 
- Communications 
- Protection
SYSCALL의 예시는 다음과 같다 (window와 Unix를 예시로)
![[Pasted image 20240418160158.png]]

---
## VM (Virtual Machine)
이게 왜 필요할까?
ex) 운영체제 코드를 조작했다가 벽돌되뿌면 우짬..
운영체제가 깔려있지만, 마치 안 깔려있는 bear hardware처럼 보여주게 하는게 VM.
![[Pasted image 20240418160335.png]]
- Java VM (JVM)
	- Java라는 언어 특징인데, 얘는 슬로건이 "한번 컴파일하면 어디서든 돌린다"이다.
	- 즉 윈도우든 리눅스든 IOS든 상관없이, JVM이라는 애가 깔려있기만 하면 어디서는 Java언어를 구동할 수 있다는 뜻.
---
# Processes and Threads
## Process
- 실행중인 프로그램
- 프로그램 실행은 Sequential 하게 일어난다.

## Process는.. 다음과 같이 구성되어 있다.
- text section : 프로그램 코드
- Stack
	- 함수 호출 및 context switch에 사용
- data section
	- static, global variable의 저장
- heap section
	- 동적할당을 위한 공간
	- + Values of Program Counter (PC) and Registers
		- MultiProgramming과 MultiTasking을 위해

## 주소공간 (address space)란?
![[Pasted image 20240418171523.png]]
프로세스가 바라보는 메모리의 구조.
- Logical 하게 연속되어 있으나 Physical 하게는 연속하지 않을 수 있음
- 이를 실제로 저장할 때는 Memory Manager의 역할이 필요함.
#### 고정 크기와 가변 크기
- 고정 크기
	- 전역변수, 코드영역은 고정적인 크기를 갖고 있음 (Data, Text)
- 가변 크기
	- Heap (동적 메모리가 할당 될 때), Stack (Function call에 따라)
그러나 Max가 무지하게 크기 때문에 Stack (Top-> down)과 Heap(down-> top)이 만날 가능성은 거의 없음

### Process State (상태)
- new 
	- 막 만들어졌음. Cpu scheduling에 포함되지 않음.
		- Personal PC에서는 쉽게 보기 어려운데(만들어지자 마자 바로 스케줄링에 편입되기 때문에), 실시간 system에서는 Process의 deadline이 있을 가능성이 높아, 새로운 프로세스를 위한 자원 할당으로 인해 deadline을 지키지 못할 가능성이 생기게 됨. 따라서 실시간 system에서 매우 중요히 다뤄지는 개념.
- running
	- CPU를 할당 받아 일을 하고 있음.
		- CPU 코어가 1개면 Running도 1개
- waiting
	- 어떤 event을 위해 대기중 (IO가 끝이 나길 기다리는 상태 같은 경우)
		- CPU를 잡으면 안되는 상태.
		- 언제 벗어남?
			- IO가 끝났다고 Interrupt가 오면 벗어남.
- ready
	- 일을 할 준비가 됐으나 CPU를 할당 받지 못해 일을 못하고 있다.
	- vs WAIT
		- wait은 IO떄문에 대기중.
		- ready는 언제든 실행 가능
			- 파란 화살표 : 프로세스가 CPU로 부터 할당받은 시간이 모두 만료되었을 때.
- terminated
	- 모든 instruction을 다 실행함. 이후 cpu가 얘를 free시켜버릴거임.
![[Pasted image 20240418171948.png]]
### Process Control Block (PCB)
> 프로세스에 대한 자료구조. (겁내 크고 복잡함) .리눅스에서는 TASK_STRUCT로 정의하고 있음.

담고 있는 정보 :
- Process state 
- Program counter 
- CPU registers 
- CPU scheduling information 
	- priority
- Memory-management information 
	- 메모리 영역은 어디에 올라가 있냐
- Accounting information 
	- 실행 중 정보
- I/O status information
	- 어떤 IO를 요청했나? 어떤 파일을 열고 있나?

### CPU Switch
![[Pasted image 20240418172537.png]]
> 프로세스 간의 switching은 이렇게 실행함.
> 그럼 다음에 어떤 프로세스로 switching 할지는 어케 결정할까? -> Scheduling algorithm에 의해 결정함.

용어정리
- Job queue : 모든 프로세스의 집합
- Ready queue :state가 ready인 프로세스의 집합
- Device queues : IO waiting 중인 프로세스의 집합

### Schedulers
- Long term scheduler , aka Job scheduler (new -> ready)
- Short term scheduler , aka CPU scheduler (ready -> running)
- Mid-term scheduler도 있음
	- Multiprogramming 상태에서의 메모리 부족 사태가 발발할 경우, 특정 프로세스를 disk로 내려버림 (Swap out) 그리고 다시 available해진 공간에 새 프로세스를 올림 (Swap in)
	- 이때 swap out 시켜 CPU를 주지 않은 상태로 만들고, 상황이 끝나 다시 Swap in 해주는 과정을 진행해주는 것이 바로 mid-term scheduler.
		- 주로 Suspend의 경우 (리눅스의 컨트롤 Z) Swap out 시켜버림.
#### Scheduler의 특징
- Short term sched는 빨라야 한다.
- Long term sched는 상대적으로 느려도 된다.
- Long term sched는 multiprogramming 의 "degree"를 결정한다.
	- 프로새스가 빠지는건 OS가 관련할 수 없지만, 새로 불러들이는건 OS가 관장하는 일이기 때문.
	- degree : 현재 CPU를 잡아서 일하고 있는 프로세스의 개수
- 이 프로세스들은 크게 두 유형으로 나뉜다.
	- IO bound
		- Short CPU burst가 많음
	- CPU bound
		- Long Cpu burst가 적음

---
### Context switch
- CPU가 다른 프로세스로 전환할때...
	- 시스템은 기존 프로세스의 정보(Context)를 저장하고, 새로운 프로세스의 데이터를 로드해와야 한다.
- 프로세스의 Context는 PCB에 있다.
	- STORE : Regs -> PCB
	- RESTORE : PCB -> Regs
- 즉 context switch가 발생할 때마다 저장 -> 전환 -> 복구를 해야한단 소린데...
	- 이경우 "Time", 즉 시간이 overhead가 된다. (저장하고, 전환하고, 복구하는 시간)
	- 하드웨어 지원에 영향을 크게 받는다
	- 그리고 Switching 하는동안 쓸모있는걸 하는게 아니라서 비효율 적이다.
	- 또 Context switching은 필연적으로 Cache flushing을 유발한다.
		- context switching 할때마다 cache를 싹 날려버림.
		- -> 새 프로세스 돌릴 때 캐시 히트가 안나니까 메모리 접근이 빈번해짐.
### Process Creation
- 프로세스는 부모 프로세스에 의해 만들어진다.
	- 즉 모든 프로세스는 부모 프로세스가 있고, 가장 위에 root, (init) 프로세스가 존재한다.
- 부모-자식 프로세스의 관계로 인해 발생하는 일들
	- Resource Sharing
		- 부모-자식 프로세스는 모든 resource를 공유한다.
		- 자식 프로세스가 부모 프로세스의 resource의 일부를 공유한다
		- 부모와 자식 간의 공유 resource가 없다.
	- execution
		- 부모는 자식프로세스가 일을 마칠때까지 기다릴 수도, 동시적으로 작동할 수도 있다.
	- Address space
		- 자식 프로세스는 부모의 주소공간의 복제본을 가질수도, 새로운 프로그램이 load될 수 도 있다.
	- in Unix...
		- `fork` 가 새로운 프로세스를 만드는 system call이다.
		- `exec`이 `fork` 다음으로 오면 프로세스의 메모리를 새로운 프로그램으로 덮어쓴다.
	- Starting point
		- `fork`SYSCALL이 오면, 자식 proc은 부모 proc과 정확히 똑같은 context를 가진다.
		- 나아가, 부모와 자식프로세스의 프로그램 실행 시점도 동일하다
			- `fork` 가 호출된 이후 ~ return 하기 까지.
		- 부모 프로세스의 `fork` return value가 자식 프로세스의 pid 인 한편, 자식 프로세스의 `fork` return value 는 0이다.
```C
#include <stdio.h>
void main (int argc, char *argv[])
{
	int pid;
	 /* fork another process */
	 pid = fork();
	 if (pid < 0) { /* error occurred */
		 fprintf (stderr, “Fork Failed\n”);
		 exit(-1);
	}
	else if (pid == 0) { /* child process */
		 execlp(“/bin/ls”, “ls”, NULL);
	}
	else { /* parent process */
		 /* parent will wait for the child 
		 to complete */
		 wait(NULL);
		 printf(“Child Complete\n”);
		 exit(0);
	}
}
```

### Process Termination
- 프로세스는 코드의 마지막 줄을 끝마친 뒤 OS에게 자신을 삭제해 달라고 요청한다 `exit`
	- 자식 proc의 데이터는 `wait`을 통해 받는다.
	- proc의 리소스가 OS에 의해 재할당 된다.
- 부모 proc은 자식 proc의 실행을 강제종료 할 수 있다 `abort`
	- 자식 proc이 할당된 자원을 다 소모하거나
	- 자식 proc의 작업 결과가 더이상 필요없거나
	- 만약 부모가 terminate 되면 자식은 모두 terminate 된다. (cascading)

### Interprocess communication
> 프로세스 간 통신

- 프로세스 각각은 독립적일 수도, 다른 프로세스와 협력할 수도 있다.
- 프로세스간 협력은 다음과 같은 이유때문에 이뤄진다.
	- 정보 공유
	- 연산속도 상승
	- 모듈성
	- 편의성
- 프로세스간 협력은 IPC (Interprocess communication)이 필요하게 된다.
- IPC 모델 두개
	- 공유 메모리
	- 메시지 패싱

#### P-C model
> producer - consumer model을 뜻한다.
- P가 데이터를 생산해서 공유메모리에 저장함 (buffer)
- C는 P가 생산한 데이터를 공유메모리에서 소비함
	- Unbounded buffer : 제한 없는 버퍼 크기
	- bounded buffer : 제한 있는 버퍼 크기

#### Message passing
> 메시지 : 공유 변수 없이 proc가 서로 통신하는 것
- IPC facility는 두 operation을 제공한다.
	- Send(msg)
	- Recieve(msg)
- 만약 P와 Q가 통신하고자 하면...
	- PQ간의 통신 링크를 만들고
	- Send-Recieve를 통해 메시지를 전달한다.
- 통신링크의 구현은
	- 물리적인 방법 (Shared mem, hardware bus)
	- 논리적인 방법 (로직 프로퍼티) 으로 가능하다.
- 동기화
	- Blocking - Sync (동기화를 함)
		- Send하면 Recieve 하기 전까지 대기
		- Recieve 하면 메시지 올 때까지 대기
	- Non-Blocking - Async
		- Send 하면 보내고 할거함
		- Recieve하면 받고 할거함
---
## Thread
### Single thread? multi thread?
![[Pasted image 20240418192115.png]]
### Definition
- 스레드란 :
	- Lightweight process 라고도 불림.
	- 하나의 스레드는 아래의 구성요소를 포함함.
		- pc
		- regiser set
		- stack space
	- 하나의 스레드는 다른 스레드와
		- Code section
		- data section (static / global vars)
		- OS resources (연 파일이나, 시그널 같은거)
	- 프로세스 (heavyweight)하나 만드는 거랑 비슷한 효과를 냄
- 어떤게 이득인가?
	- 반응성이 좋다.
		- 하나의 프로그램으로 하여금 길이가 긴 작업을 수행할 때도 안놀고 다른거 할 수 있다
	- 자원을 공유한다
		- 하나의 proc 안의 여러 thread는 같은 주소공간을 공유한다.
	- 경제적이다.
		- context switch, creation 속도가 proc보다 빠르다
	- 확장성이 좋다
		- 각 스레드는 다른 프로세서에서 병렬적으로 수행될 수 있다.
### User thread
- 스레드는 유저 단계의 라이브러리에서 처리 가능하다.
	- 스레드 생성부터, 스케줄링, 관리까지
	- 커널의 지원이 필요없다
	- 그래서 만들기가 엄청 빠르고, 관리하기 쉽다
	- 싱글 스레드 커널일 경우, 하나의 유저레벨 스레드가 blocking 하고 있으면 모든 프로세스가 다 정지될 것이다.
		- OS는 그 프로세스 안에 다른 스레드가 있는지 모르기 때문에, OS는 다른 proc한테 CPU를 줘버릴 것.

### Kernel thread
- 커널 역시 스레드를 생성, 스케줄링, 관리할 수 있다.
	- 유저레벨의 스레드보단 좀 느리다
- blocking thread는 전체 프로세스를 blocking 하지 않는다.
- MP (multi processor) 머신에선 커널이 서로다른 CPU에 스레드를 스케줄링 할 수 있다.

### 스레딩 이슈
- `fork()` `exec()`의 의미 체계
	- 만약에 멀티 스레드 환경에서 한 스레드가 `fork()` 요청을 했을 때, 몇개의 스레드가 생성되어야 할까?
		- 유닉스의 경우 application에 따라 2가지 버전의 `fork`를 제공한다.
		- `exec`은 주소공간의 모든 내용을 갈아 엎기 때문에, proc 안의 모든 thread가 영향을 받는다
		- 만약 fork 다음 exec이 바로 호출되면, 스레드를 복사하는건 별로 안중요하다
			- 왜나면 exec하는 프로그램이 필요료하는 스레드 수는 따로 있을 테니까.
		- 만약 fork 다음 exec이 바로 호출 안되면, 스레드를 다 복사해야 한다.
- thread cancellation
	- Async
		- 바로 타깃 스레드를 삭제함
		- 리소스 재분배, 공유 데이터 통합 문제가 발생함
	- Deffered
		- 타깃 스레드가 자신을 바로 삭제해야 하는지 확인하고, 순서대로 terminate 할 수 있도록 한다.
- Signal Handling
	- Unix의 경우, Signal을 통해 특정 이벤트가 발생한 proc이 어떤 유형의 proc인 지 알 수 있도록 한다.
		- Signal handler가 이 signal을 처리하기 위해 호출된다.
			- 1. Signal이 생성된다. (event로 인해)
			- 2. Signal이 프로세스에게 전달된다.
			- 3. Signal handler가 호출되어 처리한다.
		- Options
			- 시그널이 적용되는 스레드로 전달
			- 해당 프로세스의 모든 스레드로 전달
			- 해당 프로세스의 특정 스레드로 전달
			- 프로세스의 시그널을 받을 특정 스레드를 할당
- Thread pools
	- 특정 시점에 존재할 수 있는 스레드 수를 제한하는 법
		- 더 빨리 처리할 수 있도록 함
- Thread specific data
	- 각 스레드가 특정 데이터의 복사본을 필요로 할 수도 있다.
	- Transaction processing (금융 서비스같은거)
		- 같은 transaction을 여러 thread가 처리할 수 도 있다.
		- 이때, 각 스레드에는 고유한 식별자 (예컨대 tid)가 서로 달라야 한다.

---
# CPU scheduling
> process scheduling 이라고도 부름 - 프로세스의 상태를 바꾸는 것.

## Question :  CPU scheduling은 process의 유형을 구분해야 할까?
> answer : yes
- 왜?
	- proc은 크게 잦고 짧은 CPU burst가 발생하는 IO bound와 뜸하고 긴 CPU burst가 발생하는 CPU bound job으로 나뉜다.
![[Pasted image 20240418194251.png]]
- IO bound job의 반응성이 안좋으면 사용자 경험이 저하된다 (키보드가 왜 안먹지?).  
- 반면 CPU bound job은 어느정도 반응성이 안좋아도 괜찮음 (뭐, 로딩중이니까...).
	- 즉 bound에 따라 scheduling의 priority를 달리 설정해야 반응성을 높일 수 있다는 뜻.
![[Pasted image 20240418194413.png]]
- 간략화 시킨 Transition Diagram.

### CPU Scheduler
> 멀티프로그래밍 환경에서 중요시 됨
- 어떤 proc이 CPU를 잡고 있게 할 거냐?
- CPU scheduler가 메모리 상의 프로세스를 선택한다.
	- Ready 상태에 있는 proc을 선택해서, CPU를 할당한다.
- CPU scheduling decision은 "CPU가 놀때" 발생함. 즉, 프로세스가 :
	1. RUN->WAIT으로 갔을 때.
		- 강제 아님
		- IO 요청으로 인해 wait할 수 있음
	2. RUN->READY로 갔을 때.
		- RR의 경우 Time quantum이 다 됐을 때.
		- 강제임. (난 아직 더 일할수 있는데...)
	3. WAIT->READY로 갔을때
		- IO 끝났다는 인터럽트가 왔을 때.
		- 강제임
		- 이때 CPU가 왜 놀지?
			- Interrupt handler가 종료되기 때문에.
			- 원칙적으로는 CPU가 INT handler 종료 이후 원래 proc으로 돌아와야 하지만 그냥 reschedule 해버림
	4. RUN->TERMINATE로 갔을 때
		- 코드를 다 실행했으니 지금 CPU를 쓰는 애가 없음
		- 강제 아님
- 강제 : Preemptive, 강제 아님 : nonpreemptive
### Dispatcher
> ready queue 에 있는 process 중 하나를 선택하면, 얘가 실제로 CPU를 넘기는 일을 함.
- Context switch
- user mode로 switch
- 프로그램을 다시 실행하기 위한 적절한 위치로 jump
##### Dispatch latency
dispatcher가 한 process를 정지 시키고 다른 process를 실행시키기 까지 걸리는 지연시간
(대부분 Context switch overhead임)

### CPU Scheduler의 성능 지표
- CPU Utilization : MAX
	- CPU는 항상 의미있는 일을 하고 있어야 함.
- Throughput : MAX
	- 시간당 자신의 일을 완수하는 프로세스의 개수는 최대화 해야함
- Turnaround Time : MIN
	- 특정 프로세스를 완료하는데 걸리는 시간은 최소화 해야함
- Waiting time : MIN
	- Ready queue에서 대기하고 있는 프로세스의 대기 시간은 최소화해야함
- Responce time : MIN
	- 첫번째 요청이 오고난 뒤 첫번째 반응이 올때까지의 시간은 최소화 해야함.
---
## Scheduling Algorithm
### FCFS (First come First Serve)
- 먼저온놈 먼저해라.
- 아주 공평한 알고리즘
#### Case 1 (FCFS)
![[Pasted image 20240418195830.png]]
- 평균 17 tick 정도 대기했음

#### Case 2 (FCFS)
![[Pasted image 20240418200105.png]]
- 들어오는 순서만 바뀌었는데 평균 대기 시간이 엄청 차이남
	- 즉, 대기 시간이 들쭉날쭉 하다는 거임.
- Convoy effect : 호위병 효과, 완료 시간이 짧은 프로세스가 긴 프로세스 앞에오면 대기시간이 최소화 된다.
### SJF (Shortest Job first)
- cpu burst의 길이가 짧은 job부터 먼저 scheduling 함
- 두가지 방법
	- 강제적으로 (Preemptive)
		- 어떤 Job을 실행중일때, 지금 CPU burst의 length보다 작은 process 가 오면 바꿔주는 것
		- SRTF(Shortest Remaining Time First) 로 불림
	- 자발적으로 (Nonpreemptive)
		- 현재 CPU burst length 보다 더 작은 놈이 와도 안 바꿔줌.
- 이상적인 방법
	- waiting time을 최소화 할 수 있는 방법임.
#### Case 1 (SJF, nonpreemptive)
![[Pasted image 20240418200537.png]]
#### Case 2 (SRTF, Preemptive)
![[Pasted image 20240418200646.png]]
- 가장 이상적으로 waiting time을 줄일 수 있음.
#### It is Optimal! but...
- SJF와 SRTF는 가장 이상적으로 waiting time을 줄일 수 있는 알고리즘임.
- 그러나 이 두 알고리즘은 CPU의 burst time을 "알고있다"는 전제 하에 성립될 수 있음.
- 어떻게 CPU Burst의 길이를 알까?
	- 알 수 없음. 예측해야함.
	- 이전의 CPU burst 데이터를 바탕으로, 수식을 통해 유도할 수 있음.
		- $t_{n}$ :  n번째로 CPU를 잡았을 때 Burst의 길이 (실제 데이터)
		- $\tau_{n+1}$ :  n+1 번째 CPU burst의 예측값.
		- $\alpha, 0 \leq \alpha \leq 1$ : 가중치
		- $Define:\tau_{n+1}=\alpha t_{n}+(1-\alpha)\tau_{n}$
			- 예측값은 직전 값 X 가중치 와 직전에 예측했던 값 X (1-가중치) 의 합이다.
			- 근데 $\tau_{n}$도 예측값이잖아?
#### Examples of Exponential Averaging
$\alpha=0$
- $\tau_{n+1}=\tau_{n}$ 예측값은 항상 동일함.
- 최근 데이터는 중요하지 않음.
$\alpha=1$
- $\tau_{n+1}= t_{n}$
- 마지막 데이터만 중요함
이 공식을 확장하면 다음과 같이 나옴
$\tau_{n+1}=\alpha t_{n}+(1-\alpha)\alpha t_{n-1}+...+(1-\alpha)^{j}\alpha t_{n-j} + ... + (1-\alpha^{n+1}\tau_{0})$
- $\alpha$ 가 0과 1사이기 때문에 앞 계수는 점점 작아짐. 즉 $\tau_{0} = t_{0}$에 근접하게 됨.
![[Pasted image 20240418201746.png]]
- 그러면 실제로 얼마나 잘 들어맞을까?
![[Pasted image 20240418201905.png]]
### Priority Scheduling
> 얘는 일고리즘이라기 보다는 Policy (정책)에 가까움.

- priority Number가 각 proc마다 할당되어 있음
- CPU는 가장 높은 priority (가장 낮은 integer)를 가진 proc에 CPU를 할당함.
- 그렇다면 SJF는 다음 CPU burst time을 priority로 갖고 있는 priority scheduling 이라 할 수 있겠음.
#### Issues
- Starvation
	- 나보다 priority가 높은 process가 계속 나타난다... 난 언제 실행되지?
- Aging으로 해결
	- 시간이 지남에 따라 priority를 점점 높여주면 됨.
### RR (Round Robin)
- 각 프로세스 별로 time quantum 을 갖고, 이 시간이 지나면 rescheduling 하는 방식.
- 만약 n개의 프로세스가 q의 tq를 갖고, q 안에 1/n 정도의 일을 끝낼 수 있으면
	- 모든 프로세스는 (n-1)q 보다 오래기다리지는 않을 것이다
		- waiting time의 upper bound가 있음. 매우 중요한 포인트.
- 성능
	- 만약 q가 너무 길어지면 : FCFS 랑 비슷함
	- q가 너무 짧아지면 context switch overhead가 더 커져버리는 불상사가 발생함.
#### Case (RR)
![[Pasted image 20240418202514.png]]
- tq는 20 ms로 고정
### Multilevel Queue
> 이게 다 ready queue 가 하나라서 생기는 문제다.
> CPU burst 긴놈은 긴놈끼리. 짧은 놈은 짧은 놈 끼리 묶어서 ready queue 만들면 문제가 해결되지 않겠냐?
- SJF의 특징
	- Shortest first라서 자연스럽게 IO bound job에 우선권이 부여되는 효과를 가짐.
- RR의 경우, 그런거 없음.
> 그러면 반응성이 좋아야 하는 IO bound job 은 RR으로, 별로 안좋아도되는 CPU bound는 FCFS로 각각 처리하면 되지 않을까?
![[Pasted image 20240418202932.png]]
- 그럼 Queue 간의 우선순위는?
	- 먼저 Fore ground를 다 하고 background를 한다.
	- Starvation 가능성 있음
- Time Slice
	- Foreground에 많은 시간을, background에 적은 시간을 분배한다.
	- 예컨대 foreground에 80%, background에 20% 정도의 cpu tick을 분배하는 것.
![[Pasted image 20240418203440.png]]
### Multilevel Feedback Queue
- Multilevel queue의 문제점
	- 얘도 역시 CPU burst time을 알아야 Queue끼리 구분할 수 있음.
	- 이런 문제를 해결하기 위해 등장한 것이 바로 Multilevel Feedback queue.
- MLFQ에서는 proc이 queue간 이동이 가능함.
#### How to make MLFQ
1. queue 갯수 정하기
2. queue 별 스케줄링 알고리즘 정하기
3. Process의 Upgrade, Demote 정책 정하기
4. 어떤 큐에 프로세스를 넣을 지 정하기
#### Case (MLFQ)
![[Pasted image 20240418203722.png]]

# Process Syncronization
> 여러개의 process 나 thread 가 동시에 동작할 때 데이터를 안전하게 처리하기 위해 등장한 개념.

## Background
- 어러개의 프로세스가 동작할 때 (프로세스 협력 상황일 때), 공유 변수에 대한 동시적인 접근은 예상치 못한 결과를 불러올 수 있다.
- 이러한 공유 데이터의 일관성을 유지하는 것은 프로세스 협력 상황에서 올바른 순서로 실행되도록 하는 것이 보장될 때 가능해진다.
- 경쟁 상황 (Race Condition) 이란 협력적 프로세스 간에서 공유 변수에 대한 동시적 접근으로 인해 마지막으로 실행된 프로세스에 의해 공유 변수의 값이 의도치 않은 방향으로 변화하는 것을 의미한다.
> 그리고 이것을 방지하기 위해 서로 다른 프로세스간 공유변수의 접근 순서를 결정하는 것이 "프로세스 동기화" 이다.

![[Pasted image 20240422212739.png]]
이 상황에서, P1과 P2는 서로 같은 메모리상의 공유변수 X를 가진다. 만약 P1과 P2에서 동시에 X에 접근하고자 할 때, 해당 instruction이 Interleaved (끼워져서) 실행되면 어떻게 될까?
- P1 Load -> P2 Load -> P1 Increment -> P2 Decrement -> P1 Store -> P2 Store
- 최종적으로 1이 들어가게 된다. 이는 우리가 의도한 2라는 값과는 다르다....
# CS (critical section)
위 상황에서 볼수 있듯, 서로 다른 N개의 변수가 공유변수에 서로 접근하려는 경쟁적인 상황을 유발하는 코드의 부분이 존재한다. 
이를 formal 하게 정의한 것을 Critical Section이라고 부른다. 
![[Pasted image 20240422213033.png]]
- 그렇다면, 어떻게 해야 CS 문제를 해결할 수 있을 까?
	- 어떤 프로세스가 CS를 실행중일 때, 다른 프로세스의 CS로의 접근을 차단하면 된다.
		- 그거면 되나?

## Requirements for the solution
- Mutual exclusion (상호 배제)
	- 한 프로세스에서 CS가 실행중이면, 다른 프로세스는 CS를 실행하면 안된다.
- Progress (진전)
	- 아무도 CS에 들어가는 프로세스가 없으면, 들어가고자 하는 애들은 무조건 들어갈 수 있어야 한다.
	- 아무도 CS에 들어가려는 프로세스가 없는데도 CS로의 접근을 차단하는 건 프로세스의 진전 (Progress)을 막는 행위이므로 바람직하지 않다.
- Bounded Waiting
	- 들어가고자 하는데 이미 CS가 있어서 못 들어갔어. 그러다가 CS 처리하던 애가 나왔어.
	- 근데 딴 놈이 먼저 들어갔네? 무한정 기다리네?
		- 이러면 안된다. 기다리는 시간의 bound가 있어야 한다. (Starvation 금지)

# Tries to Solve this problem...
## Initial attempts
![[Pasted image 20240422213428.png]]
CS를 entry와 exit으로 감싼다.
- entry :내가 들어가도 되겠니? 이미 들어가 있는 애가 있니...?
- exit : 나 이제 나왔어!!!
	- 두 section이 유효하려면, 프로세스의 상태를 공유하는 뭔가가 있어야 겠다.
		- 즉, 공유변수 문제를 해결하기 위해, 공유변수가 필요하다! 이 무슨 모순인가.
## Swap turn
![[Pasted image 20240422213925.png]]
- Mutual Exclusion은 만족함 (turn 이 0이면 P0, 1이면 P1)
- Progress를 만족하지 않음
![[Pasted image 20240422214403.png]]
## Algorithm 2
![[Pasted image 20240422232009.png]]
이 알고리즘은 CS에 들어가고자 하는 프로세스가 있을 경우, 그 프로세스의 flag값을 true로 바꿔준다.
나머지 flag가 전부 false 이면 , 아무도 들어가고자 하는 프로세스가 없는 것이므로 CS에 진입하고, 자신의 flag를 false로 바꿔준다.
- 이 알고리즘 역시 Mutual exclusive는 성립하나, Progress에서 문제가 생긴다.
![[Pasted image 20240422232529.png]]
## Peterson's algorithm
순서를 정해버리는 "turn", 프로세스의 CS진입 의도를 알리는 "Flag"의 결합 형식
![[Pasted image 20240422232644.png]]
이 알고리즘은 Mutual exclusive, Progress를 모두 충족한다.
ㅈ![[Pasted image 20240422233433.png]]
> 그렇다면 Peterson's algorithm은 완전무결한 알고리즘일까?
- 유감스럽지만 그렇지 않다.
	- 그림에서, P0이 1이네? Turn은 내가 아니네? 대기해야겠다 -> 이 대기는 CPU를 쓰는, 하는 것도 없이 while문을 돌기만 하는 Busy Waiting이다. 이런걸 없앨 수는 없을까?
	- 또, 개발자의 입장에서 생각해보자. peterson은 CS에 대한 이해를 갖고 있어야만 코딩 이가능하다.
		- 그럼 모르면?
			- 걍 이렇게만 코딩하면 해결돼 -> primitive 한 해결책을 만들어 주면 된다. : 지극히 개발자적인 방법.
			- 그럼 우리의 Primitive는 어떻게 정의할 수 있을까? 간단하게 Lock이라는 열쇠가 하나 뿐인 CS라는 창고에 들어간다고 생각해보자. CS에 진입할 수 있는 놈은 Lock을 갖고 있는 놈 뿐이다.

## Locks
![[Pasted image 20240422233951.png]]
좋은 방법이다. 근데 이 방법이 성립하려면 한가지 "전제"가 있어야 한다. 뭘까?
> 바로 열쇠는 하나뿐이어야 한다는 거다. 이게 컴퓨터에서 쉬울까?
> 변수를 하나만 만드는건 누구나 할 수 있다. 그러나 복수의 프로세스에서 그 변수를 동시에 접근하지 못하게 막을 방법이 필요하다.
- 컴퓨터에서 열쇠를 가지고 나온다는 행위는 열쇠라는 값에 접근해서 임의의 LOAD, STORE 명령을 시행한다는 걸 뜻한다.
- 하지만 이는 다른 프로세스에서도 여전히 동시적으로 가능한 일이다. 열쇠가 복사가 된다고?
- 이 문제를 해결하기 위해 여러 시도들이 개발되었다.

### 동기화 하드웨어
> 열쇠가 하나만 존재한다는 보장을 얻기 위해서 아예 하드웨어 상으로 회로를 구워버리는 시도가 등장했다.
> 매우 직관적이고, 유용한 방법이다.

- 프로세서가 하나뿐일때 (Uniprocessor) : 인터럽트를 disable 해버림
	- Timer interrupt 꺼버려
		- Timer Interrupt off
			- CS
		- Timer Interrupt On
	- 이러면 열쇠는 하나 뿐인게 보장된다. 타이머는 프로세서당 하나밖에 없는데, 이걸 건드려 버리니까.
	- 근데 프로세서가 여러개면? 각각 타이머가 있잖아. 열쇠가 하나뿐인게 보장되지 않는다. 그리고 대부분의 현대적인 머신은 프로세서가 쥰내 많다.

- 그래서, 현대적인 머신들은 특별한 "atomic" instruction을 보장한다.
	- atomic이 뭔데?
		- 특정 함수를 수행하는 회로가 atomic 하다는 건, 이 회로는 한번에 하나의 프로세서만 접근 가능하다는 뜻이다.
			- 한번 수행되면 중간에 다른 프로세서로 context가 전환되지 않고, 무조건 일을 끝내고 나온다.
				- Test and Set (TAS), Swap등이 있다.

## Test - And - Set
- TAS가 하는 일은 간단하다.
- ![[Pasted image 20240422234956.png]]
	- 변수를 받아서, 그냥 그 변수값 그대로 리턴한다.
	- 다만 그 변수는 true로 바꿔준다.
- TAS를 이용해서 Lock을 구현하는 법
![[Pasted image 20240422235122.png]]
![[Pasted image 20240423000159.png]]
## SWAP
![[Pasted image 20240423000242.png]]
두 변수를 바꿔주는 Atomic 함수.
- SWAP을 이용해서 Lock 만들기
![[Pasted image 20240423000345.png]]
![[Pasted image 20240423001238.png]]
## Semaphore
- 소프트웨어적인 접근방법.
- Semaphore 변수 S는...
	- integer 임.
	- P(), V()라는 "atomic" 한 두 Operation으로만 그 값을 변경할 수 있음.
		- P : Wait function (대기)
		- V : Signal Function (이제 끝났어!)
	![[Pasted image 20240423001502.png]]
Semaphore 변수를 이용해 CS에 대한 Sync 보장하기
![[Pasted image 20240423001720.png]]
![[Pasted image 20240423003434.png]]
- 아니 그렇다면, Semaphore 도 P, V가 Atomic 함수여야 이게 제대로 돌아갈거 아니냐? -> 맞음
	- 근데 하드웨어 지원이 없어도 P,V 가 Atomic 하다매? 그게 어떻게 가능하냐?
		- SIngle CPU, single Core, Single Process면 간단함. interreupt를 P V 앞뒤에 껏다 키면 됨.
		- 아니면? P, V에 Peterson을 쓰면 됨. P V 의 S값에 대한 접근 자체가 CS이니, P V에 Peterson을 적용하면됨.
			- 아니 ㅅ1발 그럼 그냥 Peterson 쓰면 되잖아? --> 알못임.
				- P V 함수에만 Peterson을 써 놓으면, P V를 실제로 사용할 때는 Peterson 쓸 필요 없잖아.
					- why not algorithms?

- 잠깐, 아직 문제는 남아 있다.
	- P에서의 Busy Waiting 문제는 아직도 존재하지 않는가?
	- 그걸 이제부터 해결해 볼거임.
# Block/Wakeup Implementation
- 지금까지는 CPU를 쓰면서 기다리는 소위 "Busy Wait" 방식을 사용함.
- 만약 CPU를 쓰면서 기다리는게 아니라, 그냥 재워버리면? (SLEEP)
- 좋은 생각인거 같다. 근데 그러러면 Semaphore 변수에 약간의 수정이 필요하다.

![[Pasted image 20240423004103.png]]
P -> Semaphore를 1 감소 시킨다. 만약 Semaphore가 0 이하이면,. P를 호출한 그 프로세스의 정보를 L이라는 큐에 매단다. 이후 P를 "재운다". 
V -> Semaphore를 1 증가시킨다. 민약 Semaphore가 늘렸는데도 음수면, 누군가 기다리고 있다는 소리다.
- 만약 아무도 안 기다리고 있으면 1->0(P)->1(V) 이니까 양수가 되어야 하잖아.
	- 따라서 L에 매달려있던 프로세스 중 하나를 깨운다.
![[Pasted image 20240423004453.png]]
# Great! We can solve Proc-to-Proc Syncronization.
> 프로세스 간의 동기화 문제는 해결했다.
> 근데 과연 CS 문제가 프로세스 간에서만 발생할까?
> 글쎄..

- 인터럽트 핸들러와 커널
- 커널이 시스템 콜을 하고있을 때 Context Switching이 발생할 때
- 프로세서가 여러개일때

이때도 CS문제는 발생한다. 문제는 이제 OS가 관련되어 있다는 것.
## INT Handler vs Kernel
인터럽트 핸들러와 커널 모두 count라는 변수를 공유한다고 치자.
![[Pasted image 20240423014313.png]]
예상 결과 :+- 1이니까 0이겠지?
실제 결과 : 1이네..?
이렇듯 Interrupt Handler의 경우에도 공유변수에 접근할때 CS문제가 생긴다.
## CPU 스케줄링에서의 CS 문제
- 프로세스간 공유변수가 없어도, 시스템콜을 수행할 때 커널의 데이터에 접근하게 된다.
- 만약 커널 데이터에 접근하다가 CPU가 다른 프로세스에 넘어가면 Interleaved execution이 발생할 수 있다.
![[Pasted image 20240423015701.png]]
Proc A와 B 모두 `SYSCALL_ADDCOUNT`를 실행했으므로 스케줄링과 무관하게 커널변수 count는 최종적으로 2가 되어야 함.
그러나 proc A의 `SYSCALL_ADDCOUNT` 실행도중 Context Switch가 발생했고, Proc B가 CPU를 점유하게 되면, Proc A의 SYSCALL_ADDCOUNT가 다 완료되지 않은 상태에서 Proc B의 SYSCALL_ADDCOUNT가 실행될 수 있음. 그러면 Proc B에서 실행한 SYSCALL_ADDCOUNT의 연산 결과가 씹힐 수 있음.
- 그래서 Unix에서는 커널 모드에 들어갔을 때 CPU 스케줄링을 안해버림.
## MultiProcessor
하나의 메모리에 접근하는 CPU가 동시에 두개 이상이라면, CPU당 존재하는 Semaphore는 별 의미가 없게 됨.
![[Pasted image 20240423020124.png]]
우측 CPU의 semaphore는 우측 CPU를 쓰는 프로세스만 적용되는거지, 다른 CPU는 적용안됨.
- 이 경우 커널 프로그램 자체가 하나의 거대한 CS라고 간주해서 한번에 하나의 CPU만 커널을 사용하도록 할 수 있음 - 비효율적인 방법
- 혹은 커널이 여러개의 작은 CS들을 가지고 있게 설계할 수 있음.
---
# Classic Problems of Syncronization
> 프로세스 동기화의 개념에 대해 배웠으니 이제 실제 코딩상황에서 자주 등장하는 동기화 문제를 어떻게 해결할 것인지 배워보자.

## Bounded Buffer (Producer - Comsumer) Problem
produce-consumer 문제
어떤 proc은 데이터를 만들기만 하고, 어떤 proc은 데이터를 사용하기만 함.

소비자가 제작자가 만든 데이터를 받아오기 위해 공유메모리 공간인 버퍼가 필요함.
소비자는 제작자가 버퍼에 데이터를 쓰는 중에 읽어가면 안되고, 제작자는 소비자가 버퍼의 데이터를 읽는 중에 데이터를 쓰면 안됨

즉 이 버퍼는 동기화 문제가 존재하고. 이는 semaphore를 씀으로서 해결할 수 있음.
문제는 이 버퍼의 크기가 제한적이라는 거임.

소비자 입장에서 버퍼가 꽉찼는데도 데이터를 받아오는 속도가 느리면 제작자는 데이터를 써선 안되고,
제작자가 데이터를 만드는 속도가 느리면 소비자는 데이터를 받아오면 안된다.

즉 버퍼가 찼는지(full buffer의 개수로 확인) 안찼는지의 여부(empty buffer)를 보고 읽을지/ 쓸지 를 결정해야 한다.
그리고 이 변수 역시 소비자/제작자 가 동시에 접근할 수 있기 때문에 얘도 보호해야 한다.

### 어떻게 해결할까?
> Integer Semaphore를 사용한다.

![[Pasted image 20240423024929.png]]
- N_empty buf는 빈 버퍼의 갯수
- N_full buf 는 찬 버퍼의 갯수.
- binary semaphore mutex는 CS를 보호하기 위함.

### Case : Producer 단일 동작
![[Pasted image 20240423025643.png]]
### Case : Producer Wait
![[Pasted image 20240423025929.png]]
### Case : Consumer Wait
![[Pasted image 20240423025835.png]]
### Case : Producer/Consumer 동시 동작
![[Pasted image 20240423031127.png]]

- full = 0, empty = n(buffer의 갯수), mutex = 1로 시작함.
- Consumer는 V작업이 다 끝난뒤에 데이터를 Consume 함. "Consume Something"은 단순히 Buffer에서 데이터를 삭제하는 작업임.

## Readers - Writers Problem
- Producer-Consumer 문제와 유사하지만 Reader는 Read Only다. 즉 자료에 영향을 주지 않고 읽기만 한다.
- 따라서 Reader는 CS에 동시에 들어가도 아무 문제가 없다. 문제상황은 :
	- Reader가 읽고있는데 Writer가 쓰려는 경우
	- Writer가 쓰고 있는데 Reader가 읽으려는 경우
- 이 두가지다.
- 따라서, 이 문제를 해결하기 위해서는 Reader와 Writer의 MUTEX는 유지하면서, Reader끼리는 Concurrent한 동작을 보장해야한다. 크게 두 케이스가 있겠다.
	1. Reader가 읽고있을때, Writer가 들어오면, Reader를 더이상 받지 않는다.
	2. Reader가 읽고있을때, Writer가 들어오면, Reader를 더 받아도 된다. 이들이 다 읽으면 Writer가 들어온다. - 반응성이 좋은 방법.

이 문제의 해결을 위해 변수를 세가지 준비했다.
![[Pasted image 20240423031712.png]]
- 기본 아이디어
	- Reader - Writer : Binary Semaphore DB로 보호
	- ReadCount : Reader의 수를 나타내는 Int 변수
	- Mutex : Reader- Reader간의 Readcount 보호
- 첫번째 Reader가 들어갈때 P(db), 마지막 reader가 나올때 V(db)를 해주면 된다.
![[Pasted image 20240423031851.png]]
![[Pasted image 20240423033241.png]]
## 식사하는 철학자 문제
![[Pasted image 20240426144609.png]]
5명의 철학자가 사색중이다.
각자의 자리 앞에는 젓가락이 한쪽씩 있다.
만약 사색중 배가 고프면, 양쪽 젓가락을 한쪽씩 들어 음식을 먹으면 된다.
만약 양쪽을 다 들 수 없으면 기다린다.

이 문제는 다음과 같이 처리하면 쉽게 해결할 수 있다.
![[Pasted image 20240426144758.png]]
- 그럼 문제가 뭘까? 다음 상황을 보자.
![[Pasted image 20240426145825.png]]
> 기막힌 우연의 일치로 각 철학자가 자신의 왼쪽 젓가락을 동시에 들어버리면,
> 그 누구도 오른쪽 젓가락을 들 수 없는 상황이 발생하게 된다.

이 문제를 바로 "DeadLock"이라고 한다.
### Formal 한 문제정의
식사하는 철학자 문제에서만 Deadlock 문제가 발생하는 것은 아니다. 다음 문제를 보자.

![[Pasted image 20240426150029.png]]
두 프로세스 $P_{0}$과 $P_{1}$은 서로 S와 Q를 거꾸로 부르고 있는데, 이 경우 기가막힌 아다리로 인해 이런 문제가 발생할 수 있다.
![[Pasted image 20240426150440.png]]
이 상황에 오면 두 프로세스 P0과 P1은 서로가 서로를 기다리는, "원형 대기"의 형태를 갖게 되고, 이는 결코 풀리지 않게 된다.
두 개 이상의 프로세스가 대기하고 있는 프로세스들 중 하나에 의해서만 풀리는 이벤트를 대기하는 상황, 이를 "데드록"이라고 부른다.
- Starvation과의 비교
	- 프로세스 스케줄링에서의 Starvation과도 유사한 측면을 갖고 있는데, Starvation은 나보다 우선순위가 높은 프로세스가 계속 큐에 들어와 내가 프로세스를 실행할 가능성이 현저하게 낮아지는 것을 의미한다. Deadlock과 Starvation의 차이는
		- Starvation은 언젠간 풀린다. 나 빼고 다른 프로세스가 다 종료되면 언젠간 나한테 기회가 오겠지.
		- Deadlock은 "절대로" 안 풀린다. 
### 어떻게 해결할 수 있는가?
- 철학자들이 동시에 양쪽 젓가락을 들게 한다.
	- 철학자들은 젓가락을 2개다 든사람, 혹은 1개도 못든사람만 존재하도록 한다.
- 수리적으로 해결해본다.
	- 짝수번째 철학자는 무조건 왼쪽 젓가락을 먼저 들고,
	- 홀수 번째 철학자는 무조건 오른쪽 젓가락을 먼저 들게 한다.
- 5자리가 있으면 4명만 앉힌다.
	- Simple 한 해결법!
	
> 쉬운거 배우자고 공부하는게 아니니만큼, 우리는 첫번째 해결법 (양쪽 젓가락을 동시에 드는 방법) 을 구현해보자.
> (그건 교수님 생각이신거같은데) 

### 갑자기 딴 얘기 : Semaphore는 별로다.
왜 별로일까? 
1. 아무리 간단해졌다고는 하나 여전히 코딩하기 힘들다.
2. 정확성을 보장하기가 어렵다.
	1. 문제가 생기는 코드도 대부분은 잘 돌아간다.
	2. 정말 기막힌 딱 그상황에서만 문제가 생긴다. 관측이 어렵다는 뜻.
3. 개발자들끼리 협력을 해야한다 (!)
	1. 야 나 여기서 P 했어, 너 함수 끝날 때 V좀 해줘
	2. 골방에서 혼자 개발하는 개발자들한테 정말 너무한 처사가 아닐수없다.
4. 한번 실수하면 시스템 전체가 뻗는다.
	1. Risky한 방법이라는 뜻.
### 그럼 우리는 영원히 Sync문제를 해결할 수 없는가..?
방법이 있다. 최근의 High-level 언어들은 Monitor라는 자료형을 제공하는데, 이 자료형으로 선언된 데이터는 안전하게 프로세스 간 공유가 가능해진다.
![[Pasted image 20240426151607.png]]
- 프로세스간 공유데이터는 Private하게 보호된다.
- 해당 Variable은 Public Method를 통해서만 접근이 가능하다.
- 그리고 모니터는 Public Method의 실행에서의 Atomicity를 보장해준다.
- 어떻게?

### Monitor의 Schematic view
![[Pasted image 20240426151802.png]]
- Monitor는 Public method가 호출되면 이를 바로 실행시키는게 아니라, Entry Queue라는 큐에 집어 넣는다.
- 그리고 이 Entry Queue에서 하나씩 Pop하면서 해당 Method를 실행한다.
	- 아예 Process Syncronization 문제를 원천 차단 시켜버린다는 것.

> 그런데 만약 하나의 Public Method에서, 영향을 미친 Shared Variable이 다른 프로세스의 조건에 영향을 주면 어떡하지?
> --> 이는 Condition Variable로 정의한다.
![[Pasted image 20240426152128.png]]

### example
![[Pasted image 20240426154816.png]]
![[Pasted image 20240426154829.png]]
![[Pasted image 20240426154841.png]]
![[Pasted image 20240426154853.png]]
![[Pasted image 20240426154904.png]]
![[Pasted image 20240426154939.png]]
### Monitor로 식사하는 철학자 문제 해결하기
![[Pasted image 20240426155236.png]]
#### Step by Step
![[Pasted image 20240426173821.png]]
![[Pasted image 20240426173831.png]]![[Pasted image 20240426173842.png]]![[Pasted image 20240426173854.png]]![[Pasted image 20240426173903.png]]![[Pasted image 20240426173913.png]]![[Pasted image 20240426173923.png]]![[Pasted image 20240426173932.png]]![[Pasted image 20240426173942.png]]![[Pasted image 20240426173950.png]]![[Pasted image 20240426173958.png]]