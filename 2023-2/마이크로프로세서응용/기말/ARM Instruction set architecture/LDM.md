```asm
LDMXX rn{!}, {register_list}
```
> `rn`으로부터 4비트씩 (증가/감소) 시키며 `register_list`안의 값을 로드한다. 혹은
> `rn`으로부터 `register_list`안의 값을 로드 후 4비트씩 (증가/감소) 한다.
- `xx = IA, IB, DA, DB`

| addressing mode | Description      | Instructions     |
| --------------- | ---------------- | ---------------- |
| IA              | Increment After  | LDMIA, [[STM]]IA |
| IB              | Increment Before | LDMIB, [[STM]]IB |
| DA              | Decrement After  | LDMDA, [[STM]]DA |
| DB              | Decrement Before | LDMDB, [[STM]]DB |
- ```LDM은 LDMIA와 같다.```
- `register_list` 안의 레지스터의 순서는 상관이 없다.
	- 가장 **작은** 수의 레지스터가 가장 **낮은** 메모리 주소에 저장된다.
	- `STM R5 {R0, R1, R2} == STM R5 {R2, R1, R0}`
- Increment -> To High memory address
- Decrement -> To Low Memory address
# Example
![[Pasted image 20231129191342.png]]
- `LDM R0!, {R3, R1, R7, R2}`
	- `LDMIA` (Increment After)
		- ![[Pasted image 20231129191450.png]]
		- `Load -> Increment -> Load -> Increment...`
		- `R1 = 0, R2 = 4, R3 = 8, R7 = 12`
		- R0는 마지막 Index에서 하나 더 Increment
	- `LDMIB` (Increment Before)
		- ![[Pasted image 20231129191735.png]]
		- `Increment -> Load -> Increment -> Load ...`
		- `R1 = 4, R2 = 8, R3 = 12, R7 = 16`
		- R0는 마지막 Index
	- `LDMDA` (Decrement After)
		- ![[Pasted image 20231129192223.png]]
		- `Load -> Decrement -> Load -> Decrement...`
		- `R1 = -12, R2 = -8, R3 = -4, R7 = 0`
		- R0는 마지막 Index에서 하나 더 Decrement
	- `LDMDB` (Decrement Before)
		- ![[Pasted image 20231129192517.png]]
		- `Decrement -> Load -> Decrement -> Load...`
		- `R1 = -16, R2 = -12, R3 = -8, R7 = -4`
		- R0는 마지막 Index