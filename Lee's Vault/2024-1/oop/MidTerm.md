# Index
---
1. Get Started
2. Console Input & Output
3. Define Class
4. Array
5. Inheritance
6. Polymorphism
---
# Get Started
## Welcome to Java!
- 타깃 언어 : 자바
- Application and Applets
	- main 돌리는거 : Application
		- Windowing interface를 쓸수도 있고~ 안쓸수도 있음
	- 웹상에서 돌아가도록 작은 프로그램 : Applet
		- Windowing Interface를 사용
- 함수를 Invoke 한다, 혹은 Call 한다
- JVM (Java virtual machine)
	- Java Compiler는 프로그램을 바이트 코드로 번역한다. 바이트 코드는 운영체제에 상관없이 JVM에서 다 돌아감.
## Identifier (식별자)
- 숫자로 시작 X, 대소문자 구분함, 길이 무제한
	- Java에서 지정한 키워드는 쓰면 안됨
		- `public, class, void, static`등
	- 라이브러리에서 정의된 식별자는 쓸수는 있는데 헷갈리니까 안하는 걸 추천
		- `System, String, println`등
- Naming Conventions
	- 클래스 : Word별로 대문자
		- `FirstProgram, MyClass, String`
	- 그 외에 것 : CamelCase
		- `topSpeed, bankRate1, timeOfArrival`
## 변수 선언
> 선 선언 후 사용 가능,(,)로 연속 지정 가능

```Java
int numberOfBeans;
double oneWeight, totalWeight;
```
- 기본 자료형 (Primitive types)
	- boolean : 1byte
	- char : 2byte
	- byte : 1byte
	- short : 2byte
	- int : 4byte
	- long : 8byte
	- float : 4byte
	- double : 8byte

## Constant(상수)
> literals

숫자 e 쓸 수 있음
char : 따옴표
string : 쌍따옴표
boolean : true or false 밖에 없음. 소문자임

## string is class.
Java에 string이라는 자료형은 없음
Class String은 있음.
- Concat : +
	- String + Int : String
	- "Area" + 42 = "Area42"
- Indexes
	- Blank와 Period도 Index에 포함
- 따옴표를 포함한 String
	- `"\"따옴표\"를 씁시다" `
## Classes, Objects, Methods
- Class : Object Type의 이름
- Object : 자료 저장하고, 행동을 취하는 Actor
- Methods : Object가 행하는 Action

# Comments
```Java
// one line
/*
multiple
lines
*/
```
# Console Input & Output

## println 과 print의 차이
> println은 다음 output이 newline 뒤에 생김
> print는 같은 줄에 생김

## printf
- format Specifiers
	- d - decimal
	- f - float
	- e - e notation
	- g - general floating point
	- s - String
	- c - Character
## Importing Packages and Classes
> Java 에서 Library는 Package라고 부름

```Java
import java.text.NumberFormat;
// NumberFormat 클래스 하나만 Import

import java.text.*
// java.text안에 있는 모든 클래스 Import
```
# Defining Class

## What is Class?
- 모든 프로그램이 클래스다.
- 모든 프로그래머가 정의한 타입은 클래스다.
- 모든 지원 소프트웨어는 클래스로 이뤄져있다.

### Class Definition
```Java
public class DateFirstTry{
	public String month;
	public int day;
	public int year;

	public void writeOutput(){
		System.out.println(month + " "+ day + ", " + year);
	}

}
```

- new Operator
	- `<ClassType> objectname = new <ClassType>();`

- Methods
	- 계산해서 Value를 return 하는 함수가 있고,
	- 그렇지 않은 함수가 있다 (void)
- 전역변수와 지역변수
	- Local variable (지역변수) : Method 안에서 정의된 변수
	- 자바는 전역변수가 없다.
- Block
	- `{}`로 둘러 쌓인 것들
	- Block 안에있는게 지역, 밖에 있는게 전역
- Parameters
	- argument, actual parameters... 다 똑같음
	- 숫자, 타입 다 맞아야 함. 타입은 호환되어야 함.
- this
	- 자기 자신 object를 일컫는 키워드
	- `var = this.var` 클래스 안에서는 항상 치환 가능
	- 만약 class 내부에 있는 member variable과 이름이 똑같은 변수를 사용하려면 member variable에 대해서는 항상 this를 붙여줘야 함. 안 그러면 다 로컬로 처리해버림
- equal과 toString
	- equal과 toString은 거의 모든 클래스가 다가지고 있음.
		- equal은 객체 두개가 동일한지 안전하게 파악하기 위함임
			- `==`는 object간의 비교에선 쓰면 안됨.
		- toString은 오브젝트의 데이터를 String으로 표현하기 위함임.
- info Hiding and Encapsulation
	- info Hiding - 이 클래스가 어떻게 구현되었는지와 어떻게 사용하는지를 분리함
		- Private 키워드를 사용
	- Encapsulation - 데이터와 메서드를 하나의 클래스 객체로 결합해서 어떻게 구현했는지 숨김

### API and ADT
- API : Application Programming Interface
	- 프로그래머는 만들어놓은 클래스를 API에 맞게 갖다 쓰기만 하면 됨
- ADT : Abstract Data type
	- Info - Hiding을 적절히 사용해서 잘 숨긴 데이터 형식

### Public and Private
- Public : Instance variable이나 method가 어디서 사용되든 관련 안함
- Private : 클래스 밖에서는 IV와 method를 쓸 수 없음
	- 모든 instance variable을 private하게 만드는 것이 좋다고 알려져 있음
	- 대부분의 method는 public임. (클래스 안에서 북치고 장구치는거 아니면...)

### Accessor and Mutator (Getter & Setter)
- Accessor : 데이터 리턴해줌 (Getter)
	- 접근은 하지만 바뀌진 않아야함
	- get~
- Mutator : 데이터 조작해줌 (Setter)
	- 데이터를 잘 필터링 해줘서 setting 해야 함
	- set~
### Overloading
> 오버로딩 : 같은 클래스 내에서, 같은 함수 이름, 다른 인자 (Argument) 리스트
- 리턴 타입으로 결정되는게 아님 
	- 얘가 달라버리면 걍 이름 같은 다른 함수인데, 이걸 자바는 허용하지 않음.
- 자바에서는 어떤 메서드를 콜했을 때 인자 형식이 안맞으면 이리저리 변환해서 오버로딩 해보는데, 이때문에 의도치 않은 결과가 나올 수 있음.
	- 그래서 자바는 모호한 메서드 호출은 오류를 뱉음

### Constructor (생성자)
> Object 생성을 위한 것
- `public ClassName(anyParameters) {code}`
- 얘는 타입을 안씀, 보이드도 안씀
	- 얘네 끼리 오버로딩 됨
	- Constructor는 오직 new Operator의 사용을 통해서만 호출될 수 있음.
- 생성자에서 다른 함수를 호출할 수 있음.
	- 생성자를 호출하면 가장 먼저 IV와 Object를 생성함
		- 그다음에 다른 함수 호출 해도 됨.
		- 심지어 다른 생성자를 호출 해도 됨.
- 만약에 아무런 생성자도 정의하지 않으면, 자바가 알아서 No-Argument Constructor를 생성해줌.
	- 그러나, Custom한 생성자를 정의해버렸으면, 그거 말고는 안먹힘
	- 즉 하나 만들었으면 argument 안들어간것도 하나 만들어주는게 좋음
		- boolean은 자동으로 false
		- Class type은 자동으로 null
		- primitives는 자동으로 0
```Java
public class ThisTestClass {
	public int i;
	ThisTestClass(int i)
	{
		this.i = i;
	}
	public void writeOutput(ThisTestClass that)
	{
		System.out.println("i is " + i + ", this i is " + this.i + ", and that i is " + that.i);
		// i (Instance variable) this.i (Instance variable) that.i (that의 instance variable)
	}
	public static void main(String[] args)
	{
		ThisTestClass a = new ThisTestClass(1); // 1로 생성된 Instance
		ThisTestClass b = new ThisTestClass(10); // 10으로 생성된 Instance
		a.writeOutput(b); 
		b.writeOutput(a);
	}
}
```

### Static Method
> Object없이 호출이 가능한 메서드
- Object 대신, `ClassName.method`로 호출함
- 선언
```java
public class myclass{
	public static final double pi = 3.14;
	public static double area(double radius){
		return (pi * radius * radius)
	}
}
```
- 호출
```Java
public class mainclass{
	public static void main(String[] args){
		double r = 1.2;
		double a = myclass.area(r);
	}
}
```
> 주의 : Static method안에서 Nonstatic method를 호출하면 안됨
- Static은 특정한 오브젝트 안의 instace variable을 다루지 않기에, nonstatic한 메서드를 호출해선 안됨.
	- 하고싶으면 object를 특정하셈
		- 사실상 main 함수에서 하는 거랑 다를바 없음.
- Static 함수 안에는 this가 없음.
- 다만 Static 안에서 다른 Static을 부를수는 있음

### Static Variable
- 오브젝트가 아니라 클래스에 속하는 변수
	- 클래스별로 Static variable의 복사본은 딱 하나만 있음.
- Static method는 instance variable 에는 접근할 수 없지만, static variable에는 접근할 수 있음.
- Static variable은 선언 즉시 초기화 가능함
	- `private static int stvar = 0;`
- 얘도 기본 초기화 값이 있음
	- bool 은 false
	- 다른 primitive는 0
	- class type은 null
- 항상 초기화 해주는게 좋음
- 얘는 항상 Private여야 하고, 변하지 않는 값이면 상수값이라는 뜻인 final을 붙여주는게 좋음

### Wrapper classes
> primitives와 동일하게 행동하는 클래스를 만들고 싶을 때 사용
> 타입을 클래스로
- 굉장히 유용한 사전에 정의된 상수값과 static 메서드를 갖고있음

- byte -> Byte
- short -> Short
- int -> Integer
- long -> Long
- float -> Float
- double -> Double
- char -> Character

- Boxing
	- primitives 를 wrapper class로 바꿔주는 작업
	- `Integer integerObj = new Integer(42)`
		- 당연하게도 No-argument constructor가 없음
- Unboxing
	- wrapper class를 Primitives로 바꿔주는 작업
	- `int i = integerObj.intValue();`
		- obj.뭐시기Value꼴임
- 자동화
	- 자바 5.0부터 자동화를 지원함
		- `Integer integetObj = 42` (new 안쓰고 그냥 캐스팅 하는 것처럼 써도 됨)
		- `int i = integerObj` (메서드 콜 안하고 그냥 캐스팅 하는것처럼 써도됨)
- 유용한 상수들
	- `Integer.MAX_VALUE` 같이 최대/최소 값 지원함
	- `Boolean.TRUE` 같이 논리값도 상수로 박아둠
- 유용한 Static method들
	- parsesomething
		- 형식에 맞게 쓰여진 String을 각각의 타입으로 변환해줌
		- `Integer.parseInt("123")` 같은 식으로,
	- tostring
		- numeric value를 안전하게 string으로 바꿔줌
### Variables and Memory
- 어떤 "값"은 항상 1바이트 이상의 메모리가 필요함
- Primitives는 각각 정의된 만큼의 메모리 공간이 필요함
- 그러나 클래스타입의 변수라면, object가 위치한 메모리 주소(Reference)만 변수에 할당된 메모리 위치에 저장됨
	- 만약 특정 클래스타입의 변수 a와 b가 a=b (assignment)되면, a는 b의 메모리 주소가 담기게 됨.
		- 즉 두 변수가 하나의 메모리값을 참조하게 되기 때문에, 둘중 하나를 통해 값을 바꾸면 나머지 하나도 바뀌게 됨.
	- 한편 자바의 모든 인자는 값을 호출(Call by value)하기 때문에, 특정 메서드 안에서 인자값을 바꾼다고 원래 클래스 변수에 변화가 생기지 않음
		- 하지만 클래스 타입을 가진인자의 경우 Primitive와는 다르게 동작하는것처럼 보임
			- call by reference의 효과를 볼수 있음. 왜? 클래스타입의 변수는 메모리 주소값을 갖고있으니까.
			- 만약에 `void changeValue(Myclass cl)` 이렇게 호출했다 치면, call by value에 의해 인자 cl은 들어오는 값의 복사본으로 메서드에 들어감. 근데 그게 메모리 주소값이니까, 여기서 변경한게 인자로 넣은 클래스에 반영됨.
				- 물론 주소값은 안바뀜.
- `=`와 `==`
	- `=`는 할당 연산자임. 두 변수를 서로 같은 값을 갖도록 함
	- `==`는 비교 연산자임. 이는 클래스 타입 변수에서는 조금 다르게 작동함
		- `==` operator는 값을 비교하는게 아니라, 클래스타입이 같은 메모리 주소를 갖고있는지 검사함.
> 정리하자면, Primitive 가 인자로 들어올 경우 그 안에서 암만 바꿔봤자 원래 값은 안바뀜, 
> 그러나 인자가 클래스타입이면, 함수안에서 변화를 주면 바뀜.

### null and new
- null은 모든 클래스 타입에 할당될 수 있는 특수한 상수값임.
- "값이 없다"라는 뜻
- null은 오브젝트가 아니라, 아무런 메모리 영역도 참조하고 있지 않다는 이름표같은 것
- new는 익명의 오브젝트를 만들고, 그 주소값을 리턴해줌
- 가끔 비교를 위해서 실제 사용되지 않는 클래스타입 변수를 만들어야될 때가 있는데, 이때 new를 사용할 수 있음
```Java
if(var1.equals(new ClassName("123",1))){
	// yay
}else
	// nay
```
- 이렇듯 생성은 되었지만 할당은 되지 않은 변수를 "anonymous object"라고 부름.

### Class Protection
- 프로그램 쓸때, private한 변수가 외부 조작에 노출되는 것을 막는 것은 굉장히 중요함.
- primitive의 경우 단순히 private만 붙여주면 되지만, 클래스 타입의 변수의 경우 private가 모자랄 수 있음.

```Java
public class Person{
	private String name;
	private Date born;
	private Date died;
}
```
- Person은 하위에 Date라는 클래스타입 변수를 가지고 있음.
- Copy Constructor는 같은 타입의 object를 생성해서, 그 값을 복사해서 채워넣는 역할을 함. 여기서 Date의 copy constructor를 보면...
```Java
public Date(Date aDate){
	if(aDate == null){
		// error
		System.exit(0);
	}
	month = aDate.month;
	day = aDate.day;
	year = aDate.year
}
```
이걸 사용해야지 서로 다른 object를 새로이 생성해서 값만 복사하는게 가능해짐. 만약...
```Java
Date born = original.born;
Date died = original.died;
```
이런식으로 메모리 주소값을 복사해버리면 복사본이 아니라 그냥 원본의 메모리 주소값을 가진 원본이 들어가버림.
따라서, Person안의 Date값을 원래 Date값의 복사본으로 넣고싶으면
```Java
born = new Date(original.born);
```
의 형태로 Copy Constructor를 사용해주는게 안전함. (Deep copy)
Deep copy가 아닌건 다 Shallow Copy라 부르고, 이런 류의 Copy는 privacy 문제에 취약함.

> 주의 : Constructor가 아니라 Gette역시 Privacy 문제가 생길 수 있음.

```Java
public Date getBD(){
	return born; // 주소값을 그냥 리턴해버림. 원본을 리턴한다는 뜻.
}
public Date getBDSafe(){
	return new Date(born);
}
```
아니 그러면 String인 name도 바로 리턴하면 안되는거 아님?
- String은 mutator가 없는 클래스라서 안의 값을 바꿀 방법이 없음. 그래서 안전함. 
- 만약 String이라는 Class에 setter가 있었으면 위험했겠지. 
	- 이렇듯 Constructor 빼고 메서드가 없어서 안의 값을 바꿀 방법이 없는 클래스를 "Immutable"클래스라고 부름. 
		- 아닌건 "mutable" 클래스.
## Package and Import Statements
- 라이브러리나, 클래스를 형성하기 위해 자바는 패키지를 사용한다.
- 이를 import statement를 사용해서 특정하면, 해당 패키지를 사용할 수 있다.
### How to make Packages?
패키지를 만들려면, 만들 클래스들을 하나의 폴더안에 묶고, 각 클래스 파일에 이걸 써넣으면 된다.
```
package package_name;
```
- .class 파일만 디렉토리에 있어야 되고, .java는 있어도되고 없어도되고
- import와 package가 같이 있으면, package가 먼저와야된다.
- package name은 패키지 클래스들을 담고있는 디렉토리의 이름이다.
	- 자바는 패키지의 이름, 그리고 CLASSPATH 변수의 값만 있으면 디렉토리와 패키지를 찾을 수 있다.
		- CLASSPATH 환경변수는 OS에 따라 부여된다.
		- CLASSPATH는 보통 작업중인 디렉토리의 리스트와 동일하게 설정되어있는데,
			- 자바가 이를 돌면서 패키지가 들어가있는 디렉토리를 찾는다.
![[Pasted image 20240430221645.png]]
### Default package
현재 디렉토리에 존재하는 모든 이름없는 패키지에 속한 클래스들은 다 default package에 속한다.
CLASSPATH가 현재 디렉토리도 포함하고 있기 때문에, default package에 속한 클래스들도 자동적으로 프로그램에 속한다.

## 이름 충돌
클래스이름은 똑같은데 패키지 이름이 다르면 name clash가 발생한다.
이경우 `package_name.Classname` 처럼 앞에 패키지 이름을 명시해줘서 해결한다.
# Array
> 배열이란 무엇인가? = 같은 타입의 데이터를 저장하고 가공하기 위한 자료구조다.
## Create & Access array
- `double[] score = new double[5]`
	- `Typename[] arrayname = new Typename[arraylength]`
	- arraylength에는 int 상수, 혹은 변수가 들어올 수 있다.
	- 순화할때는 for 루프 돌리면 된다.
```Java
for(index = 0; index <5; index++){
	int curscore = score[index];
}
```

## Array is also an Object!
- 배열도 객체로 본다.
- 모든 배열은 단 하나의 instance variable을 갖는다. 이름은 length
	- length는 배열의 길이를 뜻한다.
		- `double[] score = new double[5]` 면, `score.length`는 5다.

## 배열의 초기화
`int[] age = {1,2,3}`
값을 넣으면서 초기화 할 수 있다. 이 경우 length는 자동적으로 3을 갖는다.

## String은 Char의 배열이 아니다.
```Java
char[] a = {'A', 'B', 'C'};
String b = a; // 안된다
```
하지만 String 클래스는 char\[]를 인자로 받아 String을 생성하는 생성자를 갖고있다.
```Java
String b = new String(a); // 된다
String c = new String(a, 0, 2) // 얘도 된다, 0포함 2미만의 배열 요소를 string으로 묶어준다.
```
> 배열역시 객체로 보기때문에, 클래스타입처럼 할당될때 주소값이 할당된다.

```Java
double[] a; // -> 변수
new double[10] // -> 주소값 리턴
a = new double[10]; // -> 변수에 주소값 할당
```

> 클래스타입의 배열을 만들 때, 배열만 만든다고 클래스타입의 객체까지 바로 생성되는 건 아니다.

```Java
Date[] daylist = new Date[20];
// 처음에는 daylist[0]~ daylist[20]까지 다 null이다.

for(int i = 0; i<daylist.length, i++){
	daylist[i] = new Date(); // 일일히 다 생성해줘야됨.
}
```

### Array Parameter
- 배열의 element는 인자로 들어갈 수 있다.
```Java
double n = 0.0;
double[] a = new double[10];
int i = 3;

mymethod(n);
mymethod(a[3]);
mymethod(a[i]);

// 전부다 가능하며, 0.0을 넣은것과 동일한 효과
```
- 배열 그 자체도 인자로 들어갈 수 있다.
	- 클래스 타입의 변수가 들어간거랑 같이 메모리 주소값이 인자로 들어간 거기 때문에, 함수 안에서 값 바꾸면 원래 배열도 바뀐다.
	- 즉 클래스 타입의 객체가 겪는 문제 (할당 연산자, 비교연산자) 역시 동일하게 겪는다.
### main 함수의 argument
- 메인함수는 String\[] args를 인자로 받는다.
	- 자바가 아무런 인자없이 프로그램을 돌리면, args자리에 빈 배열이 자동으로 들어간다.
```bash
java SomeProgram Hi ! there
```
- `args[0] = "Hi", args[1] = "!", args[2] = "there"`

### Returning Array
- 자바에서는 배열을 리턴할 수 있다.
```Java
public static int[] incArray(int[] a, int increment){
	int[] tmp = new int[a.length];
	for(int i = 0; i<a.length; i++){
		tmp[i] = a[i]+increment;
	}
	return tmp;
}
```
- 여기서도 a의복사본인 tmp를 리턴한다.
	- 클래스타입때와 마찬가지로, 그냥 그대로 a를 리턴해버리면 privacy problem이 발생한다.

### Enumerated types
```Java
enum Typename{val1, val2, val3 ...};
```
- 자바 5.0부터 지원하는 새로운 타입.
```Java
enum day {MON, TUE, WED, THU, FRI};
day meetingday, availday;
meetingday = day.MON;
availday = null;

System.out.println(meetingday) // 결과 : MON
```
- values()
	- `enumtypename.values()`는 해당하는 enum 타입의 배열을 리턴해줌.

## 다차원배열
```Java
double[][] table = new double[100][10]; // [세로][가로]
```
- 배열의 배열이라 생각하면됨.
- 접근방법
```Java
double[][] table = new double[sero][garo];
for(int i=0; i<sero; i++){
	for(int j = 0; j<garo; j++){
		System.out.print(table[i][j]+" ");
	}
	System.out.println();
}
```
# Inheritance
- 상속은 OOP에서 쓰는 주요 테크닉 중 하나임
- 매우 일반적인 클래스가 하나 Define 및 Compile 되고, 더 특별한 클래스가 Instance variale과 method를 추가함으로서 생성됨
- 한 클래스가 다른 클래스를 바탕으로 생성되었을 때,
	- 생성된 클래스는 Derived class
	- 원본 클래스는 base class라고 부름
- 상속은 클래스의 정의를 복사하지 않더라도 코드를 재사용할 수 있도록 하기 때문에 매우 효과적임

## Derived Class
- 직원이라는 클래스 안에 정규직, 비정규직이라는 클래스를 만들게 되면,
	- 정규/비정규직 모두 직원이라는 클래스를 상속받기 떄문에 Derived class임.
	- 이 Derived Class는 공통점도 있을 거고, 특별한 특징도 가지고 있을 것.
```Java
public class Employee{
	String name;
	int age;
	
	public Employee(){
		name = "none";
		age = 0;
	}
	public Employee(String _name, int _age){
		name = _name;
		age  = _age;
	}
	// base class! (aka, parent, ancestor class)
}

public class HourlyEmployee extends Employee{
	double wageRate;
	double hours

	public HourlyEmployee(){
		super();
		wageRate = 0;
		hours = 0;
	}

	public HourlyEmployee(String _name, int _age, double _wageRate, double _hours){
		super(_name, _age);
		wageRate = _wageRate;
		hours = _hours;
	}
	// derived cloass! (aka, subclass, child class, descendent class)
}
```
### Overriding
> Overloading과는 다르다.
- Overloading은 이름이 같은 함수의 인자가 다른 경우 발생하는 상황이고,
- Overriding은 base - derived 관계에서, 완전히 동일한 함수를 다르게 취급하는 것이다.
	- 만약 함수 앞에 키워드 `final`이 붙으면, 이 메서드는 derived class에 의해 재 정의 될 수 없다.
	- 클래스 전체에 `final`이 붙으면, 얘는 상속이 안되는 클래스다.
### Super Constructor
- `super` 생성자는 derived class에서 base class를 생성하기 위한 생성자이다.
	- `super`생성자는 derived class를 생성할때 가장 먼저 생성해야 하며, derived class의 instance variable은 super의 인자로 들어갈 수 없다.
	- 만약 derived class constructor가 `super`를 호출하지 않으면, 자동적으로 no-argument constructor가 호출된다.

### This constructor
- `this` 생성자는 하나의 생성자 안에서 같은 클래스 내 다른 생성자를 호출 할 때 사용된다.
- 만약 super와 this를 동시에 사용해야한다면, this는 super에 항상 선행한다.

```Java
public HourlyEmployee(){
	this("No Name", new Date(), 0, 0); // -> 아래 함수를 호출할 것
}

public HourlyEmployee(String _name, int _age, double _wageRate, double _hours){
	super(_name, _age);
	wageRate = _wageRate;
	hours = _hours;
}
```
### Access to Redefined Base Method
- derived class의 정의 안에서, base class 버전, 그러니까 오버라이딩함수의 원본역시 호출이 가능하다.
- `super.methodthatonceoverrided()` 의 형식으로 앞에 super.를 붙여주면 된다.
- 하지만, 클래스 정의 밖에서 derived class의 객체를 통해서는 base class의 method를 사용할 수 없다.

> base class에서 private 한 instance variable은 자식 클래스에서 by name으로 접근할 수 없다.

```Java
public class parent{
	private int privateNum;
	
}

public class Child extends parent{
	private int privateChildNum;

	public Child(){
		super();
		privateChildNum = super.privateNum // 불가능!
	}
}
```

> Private 한 method역시 마찬가지, 그러나 Public한 Method를 통해 Private 한 Method를 간접 호출했을 경우는 문제없이 작동한다.
> 대부분의 Private method가 단지 지원의 의미를 갖는 함수들이기에, 대부분의 상황에서 문제 없다.

### Protected and Package Access
`protected`는 `private`에 비해 보안 등급이 낮다.
- derived class로부터의 접근을 허용한다.(Call by name OK)

아무런 modifier가 없는 instance variable, method는 package access 권한이 있다. (friendly access로 불림)
- 이런 함수, 변수들은 같은 패키지 안에서 by name으로 바로 접근이 가능하나, 패키지 밖에서는 불가능하다.
![[Pasted image 20240501162153.png]]

### Class Object
- 자바에서, 모든 클래스는 `object`클래스의 자식이다.
- 만약 derived class가 명시적으로 정의되어있지 않으면, 자동적으로 `object`의 derived class가 된다.
- `Object`클래스는 `java.lang`에 있는 거라 언제나 자동적으로 import 된다.
	- `equals, toString`은 다 Object 클래스에 있고, 모든 클래스는 이를 상속받아 사용한다.

### equals를 올바르게 정의하는 법
- overload가 아니라, override 버전의 equals는 다음 조건을 만족해야함.
	- given class의 type이 object의 type과 동일해야함.
	- 새로운 method는 이것이 null인지 아닌지 파악해야함.
	- 그리고 비교해야함.
```Java
public boolean equals(Object otherobj){
	if(otherobj == null) return false;
	else if(getClass() != otherobj.getClass()) return false;
	else{
		Employee otherEmployee = (Employee)otherobj;
		return(name.equals(otherEmployee.name) && hireDate.equals(otherEmployee.hireDate));
	}
}
```
- getClass와 instanceOf
	- getClass는 클래스의 타입을 리턴함
	- instanceOf는 객체 뒤에 붙어서 알고자 하는 객체가 해당 클래스의 멤버에 속하는 "인스턴스인지" 테스트함.
		- 그러면 derived class도 포함되어버림.
```Java
public boolean equals(Object otherobj){
	if(otherobj == null) return false;
	else if(!(OtherObject instanceof Employee)) return false;
	else{
		Employee otherEmployee = (Employee)otherobj;
		return(name.equals(otherEmployee.name) && hireDate.equals(otherEmployee.hireDate));
	}
}

Employee e = new Employee("Joe", new Date(“January”, 1, 2004)); 
HourlyEmployee h = new HourlyEmployee("Joe", new Date(“January”, 1, 2004),8.5, 40); 
boolean testH = e.equals(h); // 얘는 True 나옴, 왜? hourlyEmployee가 Employee의 자식 클래스라 안걸림
boolean testE = h.equals(e); // 얘는 False 나옴, 왜? overriding 한 hourlyemployee의 equal은 getClass()썼으니까
```
# Polymorphism and Abstract classes
- polymorphism (다형성)은 OOP의 주요 특징 중 하나임.
- 하나의 method명으로 다양한 의미를 연관지을 수 있음.
	- late binding과 dynamic binding을 통해 실현함.
- 다형성을 사용하면 derived class의 method ㅓㅇ의를 변경할 수 있으며, 이를 base class용으로 작성된 소프트웨어에 적용할 수 있음.
### Late Binding
- 메서드 정의를 메서드 호출과 연결하는 프로세스를 바인딩이라 한다.
- 만약 메서드의 정의가 코드 컴파일될때 연결되면 early binding이라 한다.
- 만약 메서드의 정의가 runtime에 연결되면 late binding 혹은 dynamic binding이라 한다.
- 자바는 모든 메서드에 있어 late binding을 사용한다.
	- final, static, private은 예외다
	- 이 때문에, 메서드에게 아직 정의 되지 않은 부분의 일을 처리하라고 메서드를 작성할 수 있다.

### 코드로 보는 Late Binding
#### Sale
```Java
public class Sale{
	private String name;
	private double price;

	public Sale(){
		name = "no name yet";
		price = 0;
	}

	public Sale(String theName, double thePrice){
		setName(theName);
		setPrice(thePrice);
	}

	public Sale(Sale originalObj){
		if(originalObj == null){
			System.out.println("Error");
			System.exit(0);
		}
		// else
		name = originalObj.name;
		price = originalObj.price;
	}

	public static void announcement(){
		System.out.println("This is Sale Class");
	}

	public double getPrice(){return price;}
	public void setprice(double newPrice){
		if(newPrice >= 0) price = newPrice;
		else{
			System.out.println("Error!");
			System.exxit(0);
		}
	}

	public String getName(){return name;}
	public void setName(String newName){
		if(newName != null && newName != "") name = newName;
		else{
			System.out.println("Error");
			System.exit(0);
		}
	}

	public String toString(){
		return "testString";
	}

	public double bill(){return price;} //?

	public boolean equalDeals(Sale otherSale){
		if(otherSale == null) return false;
		else return (name.equals(otherSale.name) && bill() == otherSale.bill());
	}

	public boolean lessThan(Sale otherSale){
		if(otherSale == null){
			System.out.println("Error");
			System.exit(0);
		}
		return (bill() < otherSale.bill());
	}

	public boolean equals(Object otherobj){
		if(otherobj == null) return false;
		else if(getClass() != otherobj.getClass()) return false;
		else{
			Sale otherSale = (Sale) otherobj;
			return (name.equals(otherSale.name) && (price == otherSale.price));
		}
	}
}
```
#### DiscountSale
```Java
public class DiscountSale extends Sale{
	private double discount;
	public DiscountSale(){
		super();
		discount = 0;
	}

	public DiscountSale(String theName, double thePrice, double theDiscount){
		super(theName, thePrice);
		setDiscount(theDiscount);
	}

	public DiscountSale(DiscountSale origin){
		super(origin);
		discount = origin.discount;
	}

	public static void annnouncement(){
		System.out.println("DiscountSale");
	}

	public double bill(){
		double frac = discount/100;
		return (1-frac)*getPrice();
	}

	public double getDiscount(){return discount;}
	public void setDiscount(double newDiscount){
		if(newDiscount >= 0){
			discount = newDiscount;
		}
		else{
			System.out.println("Error");
			System.exit(0)l
		}
	}

	public String toString(){
		return "test2";
	}
	
	public boolean equals(Object otherobj){
		if(otherobj == null) return false;
		else if(getClass() != otherobj.getClass()) return false;
		else{
			DiscountSale otherSale = DiscountSale otherobj;
			return (name.equals(otherSale.name) && (price == otherSale.price) && (dicount == otherSale.discount));
		}
	}
}
```
#### LatebindingDemo
```java

public class LateBindingDemo{
	public static void main(String[] args){
		Sale simple = new Sale("Product A", 10.00);
		DiscountSale discount = new DiscountSale("Product A", 11.00, 10);

		if(discount.lessThan(simple)){/* cheaper*/}
		else // not cheaper

		Sale regPrice = new Sale("Product B", 9.90);
		DiscountSale spePrice = new DiscountSale("Product B", 11.00, 10);

		if(spePrice.equalDeals(regPrice)){/*equal*/}
		else // not equal
	}

}
```

### 결과
```java
cheaper
equal
```
왜 이런 결과가 나올까? Sale Class가 생성되고 컴파일 될때, DiscountSale class와 그 안의 bill() 메서드는 아직 존재하지 않았다.
> Late Binding은 Static method에는 적용되지 않는다. (Static binding)
> Static, Private, Final method에는 static binding을 적용한다.

```Java
// in main

DiscountSale discount = new DiscountSale();
Sale discount2 = discount;
discount2.announcement() // Sale version의 announcement 사용함.
```
왜 이런결과가 나올까?
- announcement는 static method이다. class이름이아니라 calling object에 따라 나온다.
	- 그래서 discount2 의 변수명인 Sale에 입각해서 Sale의 announcement를 호출한것.

### final
`final` 은 derived class에서 overriding될 수 없는 함수라는 뜻이다.

### Upcasting and Downcasting
- derived class를 base class의 변수에 할당하는 것 : Upcasting
- base class를 derived class의 변수에 할당하는 것 : Downcasting -> 아마 에러를 낼거임, 거의 하지 않는 일이기 때문에
	- instanceof operator 가 등장할 자례임
	- Downcasting 에서 reference 구분은 변수의 class type, 실제 메서드 실행은 object의 class type으로 실행된다.
```Java
public class Animal{
	String name;
	int age;

	public void makeNoise(){
		System.out.println("This is an animal")
	}
}

public class Dog{
	@override
	public void makeNoise(){
		System.out.println("Woof Woof!");
	}
	public void growl(){
		System.out.println("Grrr");
	}
}


public class Casting{
	public static void main(String[] args){
		Animal myAnimal = new Dog(); // Upcast! (base class의 변수에 child class의 주소값을 담음)
		Dog mydog = new Animal(); // DOWNCAST! DANGER!
		
	}
	public static void doAnimalStuff(Animal animal){
		animal.makeNoise(); // "Woof Woof!" (함수 Detection은 변수에 따라 진행함, makeNoise가 있나 없나~ 있네? -> 실행은 해당 메모리 주소값 (즉 Dog class type)에서 실행함)
		animal.growl(); //에러, Animal은 growl이라는 메서드가 없음.
	}


}
```
# Abstract class
모든 직원은 월급을 받는다, 하지만 부서 별로 월급이 계산 법이 다 다를 수 있다. 모든 직원이 월급을 받지만 그 세부사항은 직원 유형에 따라 다른 것. 이때 쓸 수 있는게 Abstract class이다.
```Java
public abstract class Employee{ // Abstract method 가 있으면 abstract 붙여야 된다.
	public int age;
	public String name;
	public abstract double getpay();
}

public class HourlyEmployee extends Employee{
	

}
```