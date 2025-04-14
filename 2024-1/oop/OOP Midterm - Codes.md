# System
```Java
System.out.println("abc");
System.out.print("$");
System.out.printf("%6.2f", price);
// format specifier %
// (+)6은 패딩의 개수 양수면 오른쪽 정렬, 음수면 왼쪽 정렬
// 2개의 fractional digit
// f (float)
System.out.printf("%6.2f is %s", price, name);
```
# Scanner
```Java
import java.util.Scanner
Scanner keyboard = new Scanner(System.in);

int inputInt = keyboard.nextInt();
double d1 = keyboard.nextDouble();
// whitespace (스페이스, 엔터, 탭 등)으로 구분

String w1 = keyboard.next();
// 개행문자 안가져옴
String w2 = keyboard.nextLine();
// 개행문자 가져옴

// w1에서 hi (엔터) 하면 w1은 hi가 담김. 버퍼에 개행 남음
// w2에서 개행을 받고 걍 empty string으로 끝나버림

keyboard.useDelimiter(String delim);
// 공백문자들 다 빠지고 오직 delim만으로 개행함
```
# String
```Java
int length()
boolean equals(String s)
boolean equalsIgnoreCase(String s)
String toLowerCase()
String toUpperCase()

String trim()
// 앞뒤의 공백 삭제

char charAt(int pos)
// [pos]번째 char Return

String substring(int startpos)
// startpos부터 (포함) 끝까지 묶어서 리턴

String substring(int startpos, int endpos)
// startpos부터 (포함) endpos까지 (미포함) 리턴

String indexOf(String s)
// S 가 object에 있는지 없는지 체크, 있으면 시작 Index 리턴, 없으면 -1 리턴

String indexOf(String s, int startpos)
// S가 object 안에 있는지 없는지 체크, 근데 Startpos 다음 인덱스부터 체크 있으면 시작 Index리턴, 없으면 -1

String lastIndexOf(String s)
// S가 object 안에 있는지 체크, 있으면 시작 Index 리턴, 만약 여러개 있으면 가장 마지막의 거 기준으로 시작 Index 리턴.

int compareTo(String s)
// object와 S의 Index를 비교해서, 사전 순서에 따라 결과값 출력. 만약 S와 object가 동일하면 0, 사전상 S가 뒤에있으면 음수, S가 앞에 있으면 양수, 대문자가 포함되어있는 String은 무조건 먼저임.

int compareToIgnoreCase(String s)
// compareTo와 똑같으나 대소문자 무시
```
# StringTokenizer
- words, tokens를 하나의 여러 단어 String으로 만들어주는 작업을 함

```Java
import java.util.StringTokenizer;
StringTokenizer st = new StringTokenizer("This is a test!");
while(st.hasMoreTokens()){
	System.out.println(st.nextToken());
}

public StringTokenizer(String s)
// 공백을 delimeter로 토큰을 잘라줌

public StringTokenizer(String s, String delim)
// Delim을 구분자로 String을 잘라줌

public boolean hasMoreTokens()
// 만약 String의 모든 Token을 반환하지 않았으면 true, 아니면 false

public String nextToken()
// tokenizer의 다음 토큰을 반환함.

public String nextToken(String delim)
// tokenizer의 다음토큰을 반환하는데, delim을 기준으로 자름

public int countTokens()
// 토큰의 전체 개수를 리턴함


```
# Math
- java.lang 패키지 안에 있음 (import 필요없음)
- 모든 메서드와 데이터가 Static이기 때문에, 걍 Math.~ 하면됨
- 자연상수 e와 원주율 pi가 상수로 설정되어있음
```Java
public static double pow(double base, double exponent)
// base^exponent 값 리턴

public static double/float/long/int abs(double/float/long/int arg)
// 절댓값

public static double/float/long/int min(double/float/long/int a1, double/float/long/int a2)
// 작은놈 리턴

public static double/float/long/int max(double/float/long/int a1, double/float/long/int a2)
// 큰놈 리턴

public static long/int round(double/float argument)
// 반올림한 값 리턴, 타입 변함

public static double ceil(double arg)
// 올림한 값 리턴, 타입 안변함

public static double floor(double arg)
// 내림한 값 리턴, 타입 안변함

public static double sqrt(double arg)
// 루트 씌운 값 리턴, 타입 안변함

public static double random()
// 0.0에서 1.0까지의 랜덤값 리턴함
// 더 범용적이고 유연한 Random 클래스도 존재함.
```

# Character
```Java
public static char toUpperCase(char arg)
// arg를 대문자로리턴

public static char toLowerCase(char arg)
// arg를 소문자로 리턴

public static boolean isUpperCase(char arg)
// arg가 대문자면 true, 아니면 false를 리턴

public static boolean isLowerCase(char arg)
// arg가 소문자면 true, 아니면 false를 리턴

public static boolean isWhitespace(char arg)
// arg가 공백문자면 (스페이스, 탭, 줄바꿈) true, 아니면 false

public static boolean isLetter(char arg)
// arg가 문자면 true아니면 false, 5, %이런건 문자가 아님, A~Z까지가 문자임

public static boolean isDigit(char arg)
// arg가 숫자면 true 아니면 false

public static boolean isLetterOrDigit(char arg)
// arg가 문자거나 숫자면 true, 아니면 false
```

# Enumerated types
```Java
public boolean equals(any)
// 같은 값이면 true, 아니면 false, ==써도 됨.

public String toString()
// string으로 리턴

public int ordinal()
// 해당하는 인덱스 리턴

public int compareTo(any)
// any.compareTo(any2)에서, any가 any2 보다 인덱스상 앞서면 음수, 아니면 양수, 같으면 0

public static <type>valueOf(String name)
// Day.valueOf("THU") 는 Day.THU를 리턴함.
```