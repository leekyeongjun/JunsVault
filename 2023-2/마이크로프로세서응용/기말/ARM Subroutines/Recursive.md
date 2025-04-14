# Recursive(재귀함수)
```C
int factorial(int n){
	if(n<2) return 1;
	return (n * factorai(n-1);
}
```
- 재귀함수 구현을 위해 PUSH LR과 POP LR을 잘 써야함
	- PUSH LR을 nested call 전에 호출
	- POP LR을 nested return 뒤에 호출
```asm
	.global __main

__main:
	MOV   r0, #0x03
	BL    factorial
stop:
	B     stop
factorial
	PUSH  {r4, lr}
	MOV   r4, r0
	CMP   r4, #0x01
	BNE   NZ
	MOVS  r0, #0x01
loop:
	POP   {r4,pc}
NZ:
	SUBS  r0, r4, #1
	BL    factorial
	MUL   r0, r4, r0
	B     loop
```
