| Instructions | Operands  | Description      | Flags     |
| ------------ | --------- | ---------------- | --------- |
| `CMP`        | `Rn, op2` | Compare          | `N Z C V` |
| `CMN`        | `Rn, op2` | Compare Negative | `N Z C V` |
| `TEQ`        | `Rn, op2` | Test Equivalence | `N Z C`   |
| `TST`        | `Rn, op2` | Test             | `N Z C`          |
# `CMP` and `CMN`

```asm
CMP{cond} Rn, Operand2
CMN{cond} Rn, Operand2
```
- `CMP` instruction은 `Operand2 - Rn`의 결과에 따라 Flag를 Set한다.
	- 즉 기본적으로 `SUBS` Instruction과 작동 방식은 동일하나, 그 결과값을 저장하지 않는다는 차이점이 있다.
- `CMN` instruction은 `Operand2 + Rn`의 결과에 따라 Flag를 Set한다.
	- 즉 기본적으로 `ADDS`instruction과 작동 방식은 동일하나, 그 결과값을 저장하지 않는다는 차이점이 있다.

# `TEQ` and `TST`

```asm
TEQ{cond} Rn, Operand2
TST{cond} Rn, Operand2
```
- `TST` instruction은 `Operand2 & Rn`의 결과에 따라 Flag를 Set한다.
	- 즉 기본적으로 `ANDS`와 동일하나, 결과값을 저장하지 않는다.
- `TEQ` instruction은 `Operand2 ^ Rn` 의 결과에 따라 Flag를 Set한다.
	- 즉 기본적으로 `EORS`와 동일하나, 결과값을 저장하지 않는다.
- `N, Z` Flag를 결과에 따라 업데이트 한다.
	- Operand2 의 계산 도중 C Flag도 업데이트 될 수 있다.
	- V Flag는 영향을 주지 않는다.

# If-then Statement
```C
if(a<0){
	a = 0-a;
}
x= x+1;
```
`R1 = a, R2 = x`
```asm
	CMP R1, #0
	BGE endif
then:
	RSB R1, R1, #0
endif:
	ADD R2, R2, #1
```

# Compound boolean Expression
```C
if(x <= 20 || x >= 25){
	a= 1;
}
```
`R0 = x`
```asm
	CMP R0, #20
	BLE then
	CMP R0, #25
	BLT endif
then : 
	MOV R1, #1
endif:
```

# If-Then-Else
```C
if(a == 1){
	b=3;
}else b=4;
```
`R1 = a, R2 = b`
```asm
	CMP R1, #1
	BNE else
then:
	MOV R2, #3
	B endif
else:
	MOV R2, #4
endif:
```

# For Loop
```C
int i;
int sum = 0;
	for(i=0; i<10; i++){
		sum += i;
	}
```

```asm
	MOV R0, #0
	MOV R1, #0
loop:
	CMP R0, #10
	BGE endloop
	ADD R1, R1, R0
	ADD R0, R0, #1
	B loop
endloop:
```