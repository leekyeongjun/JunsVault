# I/O Devices
- Devices : 복수의 개념
	- CPU - Device / Device - IO Mechanism
- Example : UART device
	- CPU to/from device via register read/write
	- I/O mechanism effectively transparent to CPU
![](Pasted%20image%2020231211214840.png)
- Devices는 두가지 의미를 갖는다
	- CPU-2-Device
		- CPU와 레지스터 간의 interface
	- Device to Mechanism
		- Device와 Serial port 간의 interface

# Interfacing Peripherals (주변기기 인터페이싱)
- Port-mapped I/O
	- special CPU Instruction을 이용함
- Memory-mapped I/O
	- 더 간단하고 쉬운 방법
	- 각 device register이 microprocessor의 memory address에 assigned 되어있음
	- 그냥 `LDR/STR Rn, [R,#imm]` 형식으로 접근 가능
![](Pasted%20image%2020231211215148.png)

# 접근 방식
1. define location for device
	- `.equ DEV1, 0x40010000`
		- `Dev1` 값, 즉 메모리 값을 label로 저장함
2. Read/write assembly code
	- `LDR r1, =DEV1`
		- `Dev1`의 값을(주소값) `r1`레지스터에 로드
	- `LDRB r0, [r1]`
		- `r0`레지스터에 `r1`을 주소값으로 하는 메모리 영역의 데이터 바이트를 받아옴
		- Read
	- `MOV r0, #8`
		- `r0`레지스터에 `8`값 입력
	- `STRB r0, [r1]`
		- `r1`을 주소값으로 하는 메모리영역의 데이터 바이트를 `r0` 값으로 덧씌움
		- Write
3. Equivalent in C
	- `var1 = *(char*)DEV1`
	- `*(char*)DEV1 = var1`