# IT (If - Then) instruction
```asm
IT{x{y{z}}} {cond}
x, y, z = T(then) or E(else)
```
- ARM, Thumb 모두 conditional execution을 지원한다.
	- ARM의 경우 condition이 instruction에 embedded 되어있다
	- Thumb instruction은 instruction에 남는 비트가 짧아서 condition을 표시할 수 없다.
- 통상적으로는 IT instruction을 Thumb모드로 사용한다.
- `x, y, z`는 `T or E`가 들어올 수 있다.
	- `ITTTE, ITET`등 다양하게 가능
```asm
IT    EQ
AND   r0, r0, r1   // 만약에 같으면 (Then) AND 실행
```

```asm
ITET  NE           // IF Then Else Then (조건은 NE(같지 않음))
AND   r0, r0, r1   // 만약 같지 않으면 (Then)
ADD   r2, r2, #1   // 만약 같으면 (else)
MOV   r2, r3       // 만약 같지 않으면 (Then)
```
- 중요한 사실
	- `IT` instructiond을 코드에 직접 작성할 필요는 없다.
	- Assembler는 자동적으로 condition에 따라 생성해 준다,
		- 오직 Thumb모드에서만
		- ARM 모드에서는 각자 자신의 condition을 쓸 수 있도록 하고 있다.