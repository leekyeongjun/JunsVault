| Add instruction     | Condition                        | Flag tested            |
| ------------------- | -------------------------------- | ---------------------- |
| `ADDEQ r3, r2, r1`  | Add if EQual                     | Add if Z = 1           |
| `ADDNE r3, r2, r1`  | Add if Not Equal                 | Add if Z = 0           |
| `ADDHS r3, r2, r1 ` | Add if Unsigned Higher or Same   | Add if C = 1           |
| `ADDLO r3, r2, r1 ` | Add if Unsigned LOwer            | Add if C = 0           |
| `ADDMI r3, r2, r1 ` | Add if Minus (Negative)          | Add if N = 1           |
| `ADDPL r3, r2, r1 ` | Add if PLus (Positive or Zero)   | Add if N = 0           |
| `ADDVS r3, r2, r1 ` | Add if oVerflow Set              | Add if V = 1           |
| `ADDVC r3, r2, r1 ` | Add if oVerflow Clear            | Add if V = 0           |
| `ADDHI r3, r2, r1 ` | Add if Unsigned HIgher           | Add if C = 1 & Z = 0   |
| `ADDLS r3, r2, r1 ` | Add if Unsigned Lower or Same    | Add if C = 0 or Z = 1  |
| `ADDGE r3, r2, r1 ` | Add if Signed Greater or Equal   | Add if N = V           |
| `ADDLT r3, r2, r1 ` | Add if Signed Less Than          | Add if N != V          |
| `ADDGT r3, r2, r1 ` | Add if Signed Greater Than       | Add if Z = 0 & N = V   |
| `ADDLE r3, r2, r1`  | Add if Signed Less than or Equal | Add if Z = 1 or N = !V |

```C
if(a<0){
	a = 0-a;
}
x = x+1;
```

```asm
; r1 = a, r2 = x
CMP   r1, #0
RSBLT r1, r1, #0
ADD   r2, r2, #1
```
![[Pasted image 20231201220714.png]]
# Compound Boolean expression
```C
if(x <= 20 || x >= 25){
	a = 1;
}
```

```asm
; r0 = x, r1 = a
CMP   r0, #20
MOVLE r1, #1
CMPGT r0, #25
MOVGE r1, #1
endif:
```

```C
if(a==1 || a==7 || a==11){
	y = 1;
}
else{
	y = -1;
}
```

```asm
CMP   r0, #1
CMPNE r0, #7
CMPNE r0, #11
MOVEQ r1, #1
MOVNE r1, #-1
```