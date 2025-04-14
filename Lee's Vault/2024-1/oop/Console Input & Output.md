# `System.out.println()`
- 화면에 출력하기 위한 메서드
- vs `print`
	- `println`은 괄호안에 주어진 값이 출력된 이후 `￦n`이 자동적으로 출력
	- `print`는 줄바꿈을 실행하지 않음.
- vs `printf`
	- C의 `printf`와 유사함 (format 지정)
```Java
	double price = 19.9;
	System.out.print("$");
	System.out.printf("%6.2f", price);
	// format specifier(총 6개의 char로 구성, 소수점 두번째 자리까지 기록)
	System.out.println(" each");
```
- output
```shell
$ 19.80 each

```
##  R/L Justification
```Java
	double value = 12.123;
	System.out.printf("Start%8.2fEnd", value);
	System.out.println();
	System.out.Printf("Start%-8.2fEnd", value);
	System.out.println("WoW");
```
- output
```Shell
Start    12.12End
Start 12.12   End
```

# Importing Packages and Classes
- Packages : Class의 집합, Library에 대응함.
	- Packages를 import해야함.
```Java
import java.text.NumberFormat;
// 특정 클래스를 import
import java.text.*
// java.text 하의 모든 패키지를 다 import
```

# Console Input : `Scanner` class
- prerequisites
```Java
import java.util.Scanner

Scanner keyboard = new Scanner(System.in);
```
- `nextInt`
	- 키보드에 입력되는 int 값을 받아오기
	- `int numberOfPods = keyboard.nextInt();`
- `nextDouble`
	- 키보드에 입력되는 double값을 받아오기
	- `double d1 = keyboard.nextDouble();`
- multiple input은 whitespace character로 구분한다
	- whitespace : tab, space, newline같이 안보이는 애들
- `next`
	- whitespace 가 아닌 하나의 string을 받아옴
```Java
	String word1 = keyboard.next();
	String word2 = keyboard.next();
```
- input
```Shell
jelly beans
```
- result
```shell
word1 : jelly
word2 : beans
```

- `nextLine`
	- 키보드의 엔터키가 나올때 까지 읽어들임
	- `String line = keyboard.nextLine();`
	- 이때 입력한 엔터키는 버려지게 된다.
	- 즉 `nextLine`에서 input buffer가 `'\n'` 일경우 빈 String만 들어가게 됨1