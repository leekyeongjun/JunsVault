| Instruction | Operands | Brief description         | Flags |
| ----------- | -------- | ------------------------- | ----- |
| `B`         | label    | Branch                    | -     |
| `BL`        | label    | Branch with Link          | -     |
| `BLX`       | Rm       | Branch indirect with link | -     |
| `BX`        | Rm       | Branch indirect           | -      |
- `B label` : label로 branch를 만든다
- `BL label` : 해당 instruction의 다음 instruction을 link register R14에 저장한 뒤, label로 branch를 만든다.
- `BX Rm` : 레지스터 Rm에 들어있는 주소 값으로 Branch 한다.
- `BLX Rm` : 해당 instruction의 다음 instruction을 link register R14에 저장한 뒤, 레지스터 Rm에 들어있는 주소 값으로 Branch 한다.

# Branch with Link
- `BL` instruction은 다음 instruction의 주소를 LR(Link Register)에 작성함으로써subroutine 호출을 할 수 있도록 한다.
- 이 subroutine에서 return 하려면, LR로부터 PC(Program Counter)의 값을 restore 하면 된다.
	- `MOV PC LR` or `BX LR`
- Branch instruction은 LR의 값에 영향을 주지 않는다.
## 예시
```asm
	BL foo
	/.../
foo:
	/.../
	BX LR
```
# Condition Codes
![[Pasted image 20231129201026.png]]

# Conditional Branch

| Instruction | Description                       | Flag        |
| ----------- | --------------------------------- | ----------- |
| `BEQ`       | Branch if Equal                   | `Z=1`       |
| `BNE`       | Branch not Equal                  | `Z=0`       |
| `BCS/BHS`   | Branch if unsigned High or same   | `C=1`       |
| `BCC/BLO`   | Branch if unsigned lower          | `C=0`       |
| `BMI`       | Branch if minus                   | `N=1`       |
| `BPL`       | Branch if Plus                    | `N=0`       |
| `BVS`       | Branch if overflowed              | `V=1`       |
| `BVC`       | Branch if overflow cleared        | `V=0`       |
| `BHI`       | Branch if Unsigned Higher         | `C=1 & Z=0` |
| `BLS`       | Branch if Lower or same           | `C=0 or Z=1` |
| `BGE`       | Branch if signed Greater or Equal | `N=V`       |
| `BLT`       | Branch if signed less than        | `N!=V`      |
| `BGT`       | Branch if signed Greater than     | `Z=0 & N=V` |
| `BLE`       | Branch if signed lesser or Equal  | `Z=1 or N=!V`            |
