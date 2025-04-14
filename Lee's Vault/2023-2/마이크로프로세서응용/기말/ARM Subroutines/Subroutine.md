- Subroutine은, function이나 Procedure로도 불린다.
	- single-entry / single-exit
	- exit 이후 caller에게 return
- Subroutine 호출 이후, LR(Link register)가 다음 instruction의 주소를 저장하고 subroutine exit 후 실행한다.

# Subroutine 호출
`BL label`
- LR : 다음 inst
- PC : label
`BX LR` (return)
- PC : LR
- Label은 subroutine의 이름
- Compiler는 label을 메모리 주소로 번역함
- 호출 이후, LR은 return address를 갖고 있다.

```asm
	MOV  r4 #100
	...
	BL foo

foo : 
	...
	MOV  r4, #10
	...
	BX LR
```

# ARM calling convention
![[Pasted image 20231203131528.png]]
- r0 : 32비트 arg 1. 32 비트 ret
- r1 : 32비트 arg 2
- r2 : 32비트 arg 3
- r4 : 32비트 arg 4
- \[r1 r0]  : 64비트 \[뒤 앞] arg. 64 비트 ret
- \[r3 r2] : 64비트 \[뒤 앞] arg
- \[r3 r2 r1 r0] : 128비트 arg. 128비트 ret

```C
int main(){
	int x = 3
	int y = 4
	int z = SSQ(x,y);
}
int SSQ(int x, int y){
	int z;
	z = x*x + y*y;
	return z
}
```

```asm
		MOV  r0, #3
		MOV  r1, #4
		BL   SSQ
		MOV  r2, r0
		B    ENDL
SSQ : 
		MUL  r2, r0, r0
		MUL  r3, r1, r1
		ADD  r2, r2, r3
		MOV  r0, r2
		BX LR
```

# PC realities in subroutine
- PC는 항상 4만큼 증가한다.
	- instruction의 byte가 4byte기 때문 (16 bit inst, 32 bit inst 상관 X)
		- If bit \[15-11] = 11101, 11110, or 11111, then, it is the first half-word of a 32-bit instruction. Otherwise, it is a 16-bit instruction.
