| L1      | L2   | L3   | L4   | R4   | R3   | R2   | R1        |
| ------- | ---- | ---- | ---- | ---- | ---- | ---- | --------- |
| 맨 왼쪽 | -    | -    | -    | -    | -    | -    | 맨 오른쪽 |
| 0x80    | 0x40 | 0x20 | 0x10 | 0x08 | 0x04 | 0x02 | 0x01      |

# 1. 3초 정지 
- condition : L1 R1 L4 R4
# 2. 왔다갔다
- condition : L2 R2 L4 R4
# 3. 감속
- condition : L4 R4 R1
# 4. 가속
- condition : L4 R4 L1
# 5. 회전 이후 탈출
- condition : L4 R4 L2
# 6. Rounds
- condition : L4 R4 R2

# 직진
- condition : (L4 || R4) && (!L3 || !R3)
# 후진
# 회전
- 좌
	- condition : L3
- 우
	- condition : R3
# 90도 꺾기
- 좌
	- condition : L1 L2 L3 L4
- 우
	- condition : R1 R2 R3 R4
# 감속
# 가속
# **선 유지**

직진
L4 R4

좌회전
L3