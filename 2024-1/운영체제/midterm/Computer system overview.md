# Basic elements
- processor
- main memory
	- volatile (휘발성)
	- 매우 빠르기 때문에 메인메모리로 사용
- system bus
- i/o modules
	- 2차 저장장치

# Processor
- ALU (Arithmetic logic unit)
- CU (Controll unit)
- Register (Memory)
	- 데이터 IO
		- 메모리와 CPU사이의 데이터를 주고받기 위해
		- MAR (Memory address register)
			- 주소값 특정 (LOAD STORE를 위해)
		- MBR (Memory buffer register)
			- 실제 LOAD와 STORE 수행
		- 같은 기능을 IO에도 적용한 IO address register, IO buffer register도 존재함
			- 실제로 메모리를 통해서 접근하게 됨
				- IO 디바이스의 버퍼가 매핑된 메모리 영역에 데이터 IO
	- Control and status
		- CPU의 동작, 연산결과의 상태를 저장
		- PC (Program counter)
			- 다음 instruction의 주소값 저장
			- 가지고 올때마다 하나씩 추가
		- IR (Instruction register)
			- instruction을 저장
		- PSW (Program status word)
			- condition codes
				- P/N/Z/O
			- interrupt enable/disable
				- 인터럽트 허용 여부
			- supervisor/user mode
	- User visible register
		- 프로그램에서 매우 핵심적인 부분의 optimization을 위해 직접 레지스터를 조작할 일이 생김. 이때 쓰는 Register

# Instruction execution
- Fetch -> Execute
	- Fetch :PC의 주소값에 해당하는 instruction을 가져오고 그 값을 IR에 넣는 과정
	- Execute: IR에 있는 명령어를 실행하는 과정.
		- Execute가 끝난뒤 PC의 카운터가 하나 증가
	- Instructions - 하나의 명령어는 16비트, 2바이트로 구성되어 있음
		- Processor - mem
		- Processor - IO
		- Data processing
		- Control