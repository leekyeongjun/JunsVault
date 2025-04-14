| Instruction | Operands    | Discription                |
| ----------- | ----------- | -------------------------- |
| `CBZ`       | `Rn, label` | compare and branch if zero |
| `CBNZ`      | `Rn, label` | compare and branch if non zero                           |
- CBZ와 CBNZ는 condition code flag를 바꾸지 않는다.
	- `CBZ Rn label`은 아래의 구문과 같다.
		- `CMP Rn #0`
		  `BEQ label`
	 - `CBNZ Rn label`은 아래의 구문과 같다.
		 - `CMP Rn #0`
		   `BNE label`
```C
char str[] = "hello";
int len = 0;

for(;;){
	if(*str == '\0') break;
	str ++;
	len ++;
}
```

```asm
		;r0 = string mem
		;r1 = string len
		MOV   r1, #0
loop:   LDRB  r2, [r0]
		CBZ   r2, endloop
		ADD   r0, r0, #1
		ADD   r1, r1, #1
		B     loop
endloop:
```
