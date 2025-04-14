# Load-Modify-store
```c
x = x+1;
```

```asm
LDR r0 [r1]              ; load value of x from mem
ADD r0, r0, #1           ; x = x+1
STR r0, [r1]             ; store x into mem
```
ARM assembly architecture에서 변수를 불러오고, 변경하고, 저장하기 위해서는 다음 함수들이 필요하다.
- [[LDR]]
- [[STR]]

# Byte, half-word, word를 로드하기
![[Pasted image 20231128195550.png]]
- r0 레지스터에는 ```0x02000000```값이 들어있다고 가정한다.
- Byte 로드하기
	- ```LDRB r1, [r0]```
	- ![[Pasted image 20231128195822.png]]
- Halfword 로드하기
	- ```LDRH r1, [r0]```
	- ![[Pasted image 20231128195840.png]]
- word 로드하기
	- ```LDR r1, [r0]```
	- ![[Pasted image 20231128195851.png]]
> Little endian이기 때문에 MSB가 맨 뒤에 있는 모습.

# Sign Extension
- Load Signed byte
	- ```LDRSB r1, [r0]```
	- ![[Pasted image 20231128200158.png]]
- Load Signed halfword
	- ```LDRSH r1, [r0]```
	- ![[Pasted image 20231128200206.png]]
> Signed이기 때문에 0이 아닌 -1이 default로 채워지는 모습.

# Address
- LDR, STR에서 사용되는 주소([[Address]]는 base register에서 offset을 더한 것으로 특정될 수 있다.)