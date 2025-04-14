# How to pre-index [[Address]]
```asm
LDR r1, [r0, #4]
```
![[Pasted image 20231128202303.png]]
![[Pasted image 20231128202328.png]]
- base 인 r0의 값에서 offset 4를 더한 주소값을 참조함
- 그 값을 r1 레지스터에 저장
# pre-index with Updates
```asm
LDR r1, [r0, #4]!
```
![[Pasted image 20231128202756.png]]
![[Pasted image 20231128202808.png]]
- base인 r0의 값에서 offset 4를 더한 주소값을 참조함
- 그 값을 r1레지스터에 저장
- 변화한 r0값을 업데이트