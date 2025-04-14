# Definition
```asm
LDR rt, [rs]
```
- 메모리의 주소 : rs
- 저장될 값 : 메모리상에 레지스터 rs가 저장하고 있는 주소가 가리키고 있는 값
- 저장될 장소 : rt

# Examples
```asm
LDR r1, [r0]
```

# LDR Familiy
| Name  | Usage                |
| ----- | -------------------- |
| LDR   | Load word            |
| LDRB  | Load Byte            |
| LDRH  | Load Halfword        |
| LDRSB | Load signed byte     |
| LDRSH | Load signed halfword |

# Practice
## 전제
```r0 = 0x20008000```

| mem [[Address]]  | Mem Data   |
| ---------------- | ---------- |
| ```0x20008003``` | ```0x89``` |
| ```0x20008002``` | ```0xAB``` |
| ```0x20008001``` | ```0xCD``` |
| ```0x20008000``` | ```0xEF``` |

  
```asm
LDRH r1, [r0]
```
- r1 after load : ```0x0000CDEF```
```asm
LDSB r1, [r0]
```
- r1 after load : ```0xFFFFFFEF```

# Caution
## pseudo-op
> In `LDR`, Operand Cannot be Memory Address or Large Constant
> `LDR`을 사용할 때 뒤의 인자는 메모리 주소나 큰 상수이면 안된다.

```asm
LDR R3, =0x55555555 
```
- why?
	- `LDR`은 ARM instruction이 아님, Assembler에 의해 ARM operations로 번역되는 것.
	- 그래서 32비트 상수나 심볼이 들어가면 안됨
	- Produces MOV if immediate constant can be found
	- Otherwise put constant in a “**literal pool**” and use