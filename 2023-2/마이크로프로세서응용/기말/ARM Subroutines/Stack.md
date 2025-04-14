# Cortex - M stack
- stack pointer (SP) = r13
- cortex-M은 full descending stack을 사용함
	- PUSH때 감소, POP때 증가
	- SP는 `0x20000200`에서 시작

## PUSH
- ` PUSH {rd} `
	- SP = SP -4
	- (\*SP) = Rd

- push multiple register
- `PUSH {r6, r7, r8} == PUSH {r8, r7, r6} == PUSH{r8} PUSH{r7} PUSH{r6}`
	- 레지스터의 순서는 관계 없다.
	- 여러 레지스터를 push할 때, 이들 레지스터는 자동적으로 이름순으로 정렬되며, 가장 낮은 숫자의 레지스터가 가장 마지막에 저장된다.
![[Pasted image 20231203135350.png]]
`PUSH {r3, r1, r7, r2}`
- r7 부터 차례로 push
- sp는 마지막 element의 주소
## POP
- `POP {Rd}`
	- Rd = (\*SP)
	- SP = SP+4

- pop multiple register
- `POP {r6, r7, r8} == POP {r8, r7, r6} == POP{r6} POP{r7} POP{r8}`
	- 레지스터의 순서는 관계 없다.
	- 여러 레지스터를 push할 때, 이들 레지스터는 자동적으로 이름순으로 정렬되며, 가장 낮은 숫자의 레지스터가 가장 먼저 POP된다.
![[Pasted image 20231203135456.png]]
`POP {r3, r1, r7, r2}`
- r1 부터 차례로 pop한 값 삽입
- sp는 마지막 stack의 element의 주소

```asm
	MOV  r4, #100
	BL   foo
	ADD  r4, r4, #1

foo:
	PUSH {r4}
	MOV  r4, #10
	POP  {r4}
	BX LR
```
![[Pasted image 20231203150924.png]]
![[Pasted image 20231203151056.png]]

# Example
![[Pasted image 20231203151231.png]]
- SP는 초기값 `0x20000200`가리킴
- PC는 실행할 instruciton `0x08000138`가리킴
![[Pasted image 20231203151318.png]]
- SP 변화없음
- PC는 실행한 inst `0x0800013C`가리킴
![[Pasted image 20231203151354.png]]
- SP 변화없음
- BL inst에 의해 PC는 QUAD의 inst인 `0x0800014C`로 이동
- 이때 LR은 subroutine 실행후 돌아와서 실행한 inst의 주소인 `0x08000140`저장
![[Pasted image 20231203151514.png]]
- PUSH {LR}에 의해 Stack 을 4만큼 감소, 해당 주소값에 LR의 값인 `0x08000140`저장
- 그 다음 inst인 BL SQ 실행
	- PC는 SQ의 주소값인 `0x08000144` 가리킴
	- LR은 그 다음 inst의 주소값인 `0x08000754`가리킴
![[Pasted image 20231203151801.png]]
- MUL R0 R0 으로 인해 R0 값은 `0x04`가 됨
- PC는 다음 inst 인 `0x08000144`가리킴
- LR은 변화 없음
- SP 변화없음
![[Pasted image 20231203151904.png]]
- BX LR inst로 인해 PC 값은 현재 저장되어 있던 `0x08000154`로 이동
- 나머지는 변화 없음
![[Pasted image 20231203151953.png]]
- 또 BL SQ 이므로 SQ의 주소값인 `0x08000144`로 이동
- LR의 값은 그 다음 inst인 `0x08000158`로 이동
![[Pasted image 20231203152045.png]]
- MUL R0, R0을 실행해서 R0의 레지스터 값은 16이 됨 (오타임)
- PC는 다음 inst 인 `0x08000148`을 가리키게 됨
- 나머지는 변화없음
![[Pasted image 20231203152156.png]]
- BX LR에 의해 PC의 값은 LR의 값으로 변함.
- 나머지는 변화없음
![[Pasted image 20231203152223.png]]
- POP{LR}로 인해
	- LR은 `0x08000140` 값으로 바뀜
	- SP는 4증가
- PC는 다음 inst인 `0x0800015C`를 가리킴.

![[Pasted image 20231203152328.png]]
- BX LR의 실행으로 인해 PC는 LR의 값인 `0x08000140`값으로 변함
- 나머지는 변화 없음
- 이후 ENDL로 branch 이후 종료
# SP의 초기화
- stack을 사용하기 전, software는 stack 공간을 define 하고, SP를 initialize해야한다.
- 통상적으로, assembly file `startup.s`에서 stack space를 규정하고 SP를 초기화 한다.
	- `LDR SP, = __stack_top__`
	- Cortex-M 은 자동적으로 SP를 벡터테이블의 첫 4바이트값으로 초기화하는 메커니즘을 가지고 있다.