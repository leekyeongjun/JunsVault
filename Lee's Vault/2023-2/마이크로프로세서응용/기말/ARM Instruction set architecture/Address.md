# Address in ARM Assembly
- word와 unsigned byte에 대한 [[LDR]], [[STR]] 접근에 대해, offset 값은 다음과 같이 정해질 수 있다.
	- 12bit의 immediate value(상수)
		- ```LDR r0, [r1, #8]```
			- r0레지스터에 r1의 값에 8만큼을 더한 주소값의 메모리 로드
	- 레지스터 혹은 상수에 의해 shift된 레지스터
		- ```LDR r0, [r1,r2]```
			- r0레지스터에 r1의 값에 r2의 값만큼을 더한 주소값의 메모리 로드
		- ```LDR r0, [r1, r2, LSL#2]```
			- r0레지스터에 r1의 값에 r2를 Logical Left Shift 2만큼 한 값을 더한 주소값의 메모리 로드
- halfword와 signed halfword, byte에서 offset의 값은 다음과 같이 정해질 수 있다.
	- 8bit의 immediate value(상수)
	- 레지스터 (shift 안됨)
	
| Case              | Immediate value's bit | shiftable |
| ----------------- | --------------------- | --------- |
| unsigned byte     | 12                    | O         |
| signed byte       | 8                     | X         |
| unsigned halfword | 8                     | X         |
| signed halfword   | 8                     | X         |
| word              | 12                    | O          |
- [[pre-indexed]] 혹은 [[post-indexed]] 방법 중 하나를 택하면 된다.

| Index format          | Example                 | Equivalent                  |
| --------------------- | ----------------------- | --------------------------- |
| pre-index             | ```LDR r1, [r0, #4]```  | r1 = mem[r0+4], r0 unchange |
| pre-index with update | ```LDR r1, [r0, #4]!``` | r1 = mem[r0+4], r0 += 4     |
| post-index            | ```LDR r1, [r0], #4```  |     r1 = mem[r0], r0 += 4                        |
