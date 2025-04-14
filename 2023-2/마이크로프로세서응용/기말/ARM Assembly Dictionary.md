# Expression
## 상수
```
#0

Assembly에서, 상수 값은 앞에 #를 붙인다.
```
- `#4` 십진 정수
- `#0x1A` 16진수 정수
- `#07` 8진수 정수
- `#0b0100110` 2진수 정수
- `#-3` 음수

## 심볼
- 식에서 참조할 수 있는 키워드
- 그냥 마침표 `.` 만 쓰이는 경우는 현재 위치를 말함

## 연산자
```
+ - ~ 
```
- 단항 연산자 (Unary)
	- + - ~
- 이진 연산자 (Binary)
	- + - * / %
- 이진 논리 연산자 (Binary Logical)
	- && ||
- 이진 비트와이즈 연산자 (Binary Bitwise)
	- & | ^ >> <<
- 이진 비교 연산자 (Binary comparison)
	- == != < > <= >=

# Label
```
my_label :
```
- Instruction의 시퀀스도, 값도 될 수 있는 말 그대로 "이름표" 
- 특정 위치를 가리키는 "식별자"
# Directive
```
.Directive
```
- 어셈블러에게 특정 작업을 수행하도록 하는 명령어이다. 
### Directive의 리스트
- Definitions - Label과 함께하면 상수로 쓸 수 있도록 정의하는 Directives
	- `.ascii "string"`
		- 문자열 저장. `Null byte`를 추가하지 않는다.
	- `.asciz "string"`
	- `.string "string"`
		- 위 두개는 저장한다.
	- `.byte data, data`
		- 1바이트 데이터 저장
	- `.hword data, data`
		- 2바이트 데이터 저장
	- `.word data, data`
		- 4바이트 데이터 저장
	- `.quad data, data`
		- 8바이트 데이터 저장
	- `.octa data, data`
		- 16바이트 데이터 저장
- Alignment : 데이터 정렬
	- `.balign byte, value`
		- `byte` 단위로 정렬, 남는 공간에 `value` 삽입
		- `byte`는 2의 거듭제곱 형태
	- `.p2align expo, value`
	- `.align expo, value`
		- 2의 `expo` 승 단위로 정렬, 남는 공간에 `value` 삽입
- Space filling : 데이터 공간 채우기
	- `.space count, value`
		- `value`값 으로 채워진 `count` 바이트 내보내기
	- `.fill count, size, value`
		- `size`길이만큼의 `value`값을 가지는 `count`바이트 내보내기
- Macro : C에서의 함수 정의와 유사함.
	- 
		```
		.macro macro_name, arg1:req, arg2=#4, arg3:varag,
			movw \arg1, #\arg2 + #\arg3
			.exitm
		.endm
		```
- Org
	- `org location vaule`
		- `location`은 byte로 표현되거나, 섹션기호 사용 가능
		- `value`는 1바이트 값
- Conditional
```
.if conditiom
.elseif condition
.else condition
.endif
```

# Instruction
`LSL Rd Rn ` : `Rd`를 `Rn`만큼 Logical Left shift
`LSR Rd Rn` : `Rd`를 `Rn` 만큼 Logical Right shift
`ASR Rd Rn` : `Rd`를 `Rn` 만큼 Arithmetic 