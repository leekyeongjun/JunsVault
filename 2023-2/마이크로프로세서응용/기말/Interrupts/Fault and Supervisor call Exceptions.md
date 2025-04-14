# Bus Faults
## 유발 상황
> 버스 폴트는 AHB interface transfer 중의 에러 감지시 생성된다.
- Prefetch abort(Instruction prefetch)
- Data abort (data read/write)
- stacking error
- unstacking error
## 언제 발생하는가?
- Invalid memory region에의 접근
- unready target device 에 대한 접근
- privilege level 에서 지원하지 않는 transfer size를 가진 target device에의 접근
# Memory Menagement Faults
## 유발상황
- MPU (Memory protection Unit)에 의해 define 됮 않은 메모리 영역 접근
- nonexecutable memory region에서의 코드 실행
- RO 영역에 write
- Privileged only memory 에 User state로 접근
# Usage Faults
## 유발상황
- **Undefined instructions**
- **coprocessor(보조 프로세서) instructions**
- **ARM state로의 전환 시도**
	- PC가 LSB가 0인 값으로 바뀔 떄
- **Invalid Interrupt return**
	- LR의 value가 invalid
- Multiple load & store 로 인해 발생한 **Unaligned** memory access
- **interrupt 가 살아있는데 Thread mode로 돌아오는 경우**
언제 발생하는가?
- Divide by zero
- unaligned memory access
# Hard Faults
## 유발상황
- usage, bus, memory fault로 handled 될 수없는 경우
- bus fault로 인해 vector table을 fetch 할 수없는 경우
- 위 세개의 fault가 다른 더 높은 우선순위를 가진 fault의 **handling 도중 발생하는 경우**
# SVC(Supervisor call)
- svc instruction에 의해 발생
- 소프트웨어를 더 portable 하게 해준다
	- 유저 애플리케이션이 하드웨어의 프로그래밍 디테일을 몰라도 되게 해주니까
	- ![[Pasted image 20231211094158.png]]
# PendSV(Pended supervisor call)
- ICSR의 PENDSVSET-비트애 의해 발생
	- 가장 낮은 privileged level을 갖는다
	- OS가 exception을 pend하도록 한다 그래서 더 중요한 것들을 하려고 한다(더 높은 우선순위로)
		- context switching에 사용된다.
### 문제상황 예시
![[Pasted image 20231211094344.png]]
- SYSTICK 이 context switching을 유발한다고 가정하자
![[Pasted image 20231211094555.png]]
- 만약 SYSTICK이 interrupt handling 중 발생하면, context switching이 발생한다.
	- usage fault도 생성된다.
	- 결론적으로 SYSTICK interrupt handling의 지연이 발생한다.
### 해결책
- OS가 interrupt handler 실행도중에는 context switching을 안하면 문제가 해결된다.
- 하지만 이경우 SYSTICK처럼 계속 뜨는 interrupt에 대응하다 보면 context switching이 엄청 느려진다.
### 더 나은 해결책
- PendSV를 쓰는 것이 낫다
	- SYSTICK exception이 그들 자신에 의해 context switching을 실행하지 않도록 하면 된다.
		- 가장 낮은 priority 일 때 pendSV exception을 pend한다
	- PendSV Handler는 다른 우선순위가 높은 interrupt를 끝낸 뒤에 context switching을 안전하게 실행한다
![[Pasted image 20231211102022.png]]]