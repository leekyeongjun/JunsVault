# Objects and methods
- Java is OOP(Object oriented programming) language
- Class vs Object
	- Object는 Class 의 한 Instance이다.
	- Class는 Data type이다.
- 이 Object를 Method를 통해 상호작용하여 (정보를 얻거나, 요청하거나, 상호작용 하는 것) 프로그램을 구성하는 프로그래밍 방법이 OOP이다.
# Terminology Comparison
- methods - functions - subprogrames - procedures
	- Java에서 Method는 Class의 일부이다.
	- Method는 Class를 구성하는 Method의 일부이다.

# Java application programs
- application vs applets
	- application : starts with `main`
	- applet : web browser상에서 수행할 수 있는 작은 size의 program

# First Program
```Java
public class FirstProgram{ // name of class // 모든 기본 단위는 class이다.
	public static void main(String args[]) // main method
	{
		System.out.println("Hi");
		//system.out이라는 객체에 포함되어있는 println method를 지칭하기 위해 사용
		System.out.println("Welcome to Java");
		System.out.println("Calc...");

		int answer;
		answer = 2+2;
		System.out.println("2 + 2 = " + answer);
		// + 뒤의 integer를 자동적으로 4가 됨
	}
}
```

# Byte code and JVM(Java virtual machine)

- 일반적인 컴퓨터 언어의 경우, 코딩 이후 compile하여 object code를 만들어낸다.
	- machine의 종류에 따라 다른 컴파일러를 사용해야 한다.
- 하지만 Java에서는 java compiler가 Java program을 byte code로 complie한다.
	- 이후 byte code가 머신에서 머신코드로 변환되어 실행된다.
		- 이를 변환시켜 주는 (byte code to machine code) 게 JVM(Java virtual machine)이다.
			- highly portable

# Identifiers
> 프로그램을 구성하는 컴포넌트의 이름
- 시작은 문자나 ￦_로 시작한다.
- 두번째 부터는 숫자가 와도 된다.
- 길이제한은 없으며, 대소구분을 한다.
- Keywords : 프로그래밍 언어에서 사용하려고 만든 id
	- `public class void static`등
- Predefined identifiers : Java의 standard library에서 쓰는 id
	- `System String println`등

# Naming conventions
- 변수나 method, object 인경우 시작은 소문자로 하고, 두 단어를 붙였을 경우 두번째 단어가 시작하는 위치를 대문자로 만든다.
	-  `topSpeed bankRate1 timeOfArrival`
- class인경우 대문자로 시작함
	- `FirstProgram MyClass String`

# Variable declaration
- Java에서는 변수를 쓰기 전에 항상 선언해야한다.
- Primitive type (Basic type, Built-in type)
	- `int numberOfBeans;`
	- `double oneWeight, totalWeight;`
- Class type (Derived type, User-defined type)
	- 유저가 선언한 확장형 type

# Constants
- 대부분 C랑 같음

# Class "String"
- String은 System class로 지정되어있다. basic type이 아님
	- Null-terminated character array