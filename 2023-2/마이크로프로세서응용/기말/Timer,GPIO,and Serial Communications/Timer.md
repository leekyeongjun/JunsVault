# What is Timer?
- Free run counter
	- processor와는 독립적으로 동작함
- Functions
	- Input capture
	- output compare
	- pulse-width modulation (PWM) generation

## Clock
![](Pasted%20image%2020231211220224.png)
clock은 특정 주기마다 인터럽트를 생성시킨다.
- CL_PSC : CPU의 클럭을 의미한다. 매우 주파수가 높다
- PSC(Prescaler) : 설정된 값에 따라 매우 주파수가 높은 CL_PSC의 주파수를 낮춰 CL_CNT로 만든다
	- CL_CNT = CL_PSC / PSC + 1
- Counter : 클럭의 펄스마다 카운트 다운값을 1씩 감소시킨다
	- 카운트다운 값이 0이되면, Interrupt를 발생시킨다. (ISR)
- ARR : 카운터 값이 0이되거나, 새로운 카운트 사이클이 시작할 때 자동적으로 복원할 값을 저장하는 레지스터다.

## Input capture && Output compare
![](Pasted%20image%2020231211220839.png)
CCR(compare and capture register)는 비교, 캡처를 담당한다
- 비교(output compare): CCR에 저장된 값과 카운터에 저장된 값이 같은지 지속적으로 확인하고, 같으면 특정 작업을 수행하도록 할 수 있다.
	- 이때의 타이머 결과를 `OCREF`라고 한다.
		- 이는 주로 PWM을 생성하거나, 특정 시간 간격에 따른 작업을 수행하는 데 사용된다.
- 캡처(input capture) : 외부 입력신호의 상태변화를 캡처한다. 
	- 외부 입력 핀의 상태가 변화되면, 그때의 타이머 카운터 값이 CCR에 바로 저장된다.
	- 주로 입력신호의 주기나 펄스 폭을 측정하는 데 사용한다.

### Output compare(비교)의 모드 값에 따른 변화
![](Pasted%20image%2020231211221213.png)
- constant value (프로그래머가 설정한 값)
- Timer counter (Timer에 의해 주기를 갖는 값)
- CCR은 위 두 값이 같은지 지속적으로 비교하고, 같으면 OCREF를 뱉는다.
	- 그 OCREF는 Output compare mode (OCM)에 의해 달라진다.
- 모드 값은 다음과 같다
	- 000 - Frozen (동작하지 않는다)
	- 001 - High true
	- 010 - Low true
	- 011 - Toggle
	- 100 - always low
	- 101 - always high

### PWM (pulse - width - modulation) 생성
![](Pasted%20image%2020231211221516.png)
- 위 그림을 보면, counter의 값이 reference signal을 넘을 때 PWM output이 상승하는 것을 볼 수 있다.
- 이런식으로 clock에 따른 지속적인 pulse signal을 만들어주는 것이 PWM이다.
- PWM은 mode에 따라 다르게 동작한다.
- ![](Pasted%20image%2020231211221631.png)
	- low 모드에서는 reference signal 보다 **낮으면** active, 높으면 inactive다. 그래서 low true다.
	- 반대로, High true 모드에서는 reference signal 보다 **높으면** active다.
#### Low true mode 예시
![](Pasted%20image%2020231211221803.png)
- CCR (Reference) value는 3이고. ARR(초기화 할 기준 값) value는 6이다.
- Low true 모드일 때를 보자.
	- Counter value가 0,1,2 일때만 PWM이 켜져 OCREF가 active 한 모습이다.
	- Counter value가 3~6 이면 PWM이 Inactive해져서 OCREF가 inactive하다.
- 이때 Duty Cycle은 `(CCR)/(ARR+1)`이므로, 위 케이스에서는 3/7 이다.
- period는 `(1+ARR)*clock period` 이므로, 위 케이스에서는 7*`clock period`이다.
#### High true mode 예시
![](Pasted%20image%2020231211222224.png)
- 같은 상황에서 High true 모드 일 때를 보자.
	- 이번에는 Counter value가 CCR(reference value)인 3보다 크거나 같을 때 PWM을 생성해 OCREF를 active 하게 만든다.
- 이때 Duty cycle은 `1-(CCR/(ARR+1))` 이므로 4/7 이다.
	- High true의 Duty cycle과 low true의 duty cycle을 더하면 반드시 1이다.
- Period는 같다.