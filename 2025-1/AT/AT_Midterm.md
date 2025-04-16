# Three Basic Concepts
## Language
Alphabet을 가지고 만든 임의의 String들의 Subset.
$L= \{a^{n}b^{n}, n \ge 0\}$
## Alphabet
$Σ = \{a,b\}$
String을 만드는 데 사용하는 Terminal
## String
$w = abab, aaabb ...$
alphabet을 0개 이상 사용하여 만든 문자열
### Substring
특정 String에 속하는 String
- Prefix : $w = abab$, prefix $p = \{a, ab, aba, abab\}$
- Suffix : $w = abab$, suffix $s = \{b, ba, bab, baba\}$

# Symbols Dealing With Language, Alphabet and String
$w = a_{1}a_{2}a_{3}...a_{n}$
$v =b_{1}b_{2}b_{3}...b_{m}$

- $wv = a_{1}a_{2}a_{3}...a_{n}b_{1}...b_{m}$
- $w^{R} =a_{n}a_{n-1}a_{n-2}...a_{1}$
- $|w| = n$
	- $|λ|=0$
- $w^{n}$
	- $w^{0}= λ$
	- $w^{1}= w$
	- $w^{2}=ww$
- $Σ^{*}$
	- $Σ$안에 있는 모든 alphabet을 써서 만들 수 있는 모든 String의 집합
		- $λ$포함
- $Σ^{+} = Σ^{*}-\{λ\}$

$L=\{a,aa,aab\}$
- $a,aa,aab$ 각각을 sentence라고 한다.
- $\overline{L} = Σ^{*}-L$
- $L^{R}= \{w^{R}:w ∈ L\}$
- $|L| = 3$
- $L^{n}=LLLLL....$
- $L^{0}=\{λ\}\ne\{ \}$
- $L^{*}=L^{0}∪L^{1}∪L^{2}∪...$
- $L^{+}=L^{1}∪L^{2}∪L^{3}∪....$ 

$L_{1}=\{a,aa\}$
$L_{2}=\{b,bb\}$
- $L_{1}L_{2} = \{ab,abb,aab,aabb\}$

$|L_{1}| = n, (n \ge 0)$
$|L_{2}| = m, (m\ge0)$
- $|L_{1}L_{2}|\le nm$
	- 중복되는 문자열이 있을 수 있기 때문이다.

# Grammer
$G = (V,T,S,P)$
- Variables
- Terminal Symbols
- Start variables
- Productions

$G = (\{S\},\{a,b\},S,P)$
$P:$
- $S → aSb$
- $S → λ$

## Derivation
$S ⇒ aSb ⇒ aaSbb ⇒ aabb$
- $⇒$ - Derivation Arrow
- $aaSbb$ - 이러한 꼴을 Sentential Form이라고 한다.
- $aabb$ - Terminal을 다 소모한 상태로 Derivation이 완료되었으므로, 이를 Sentence라 하며, 이 Sentence는 Language에 포함된다.
## Definition of Language with Grammer
$L(G)=\{w ∈ T^{*}:S ⇒^{*}w\}$
- Grammer $G$에 의해 생성되는 Language $L$은,
- Start Symbol $S$로부터 만들수 있는 모든 string $w$의 집합이며,
- $w$는 Terminal $T$의 원소로만 이뤄진다.
## Language to Grammer 
$L = \{a^{n}b^{n+1}:n\ge0\}$ 을 Grammer로 표현하려면?
- $L = \{a^{n}b^{n},n\ge0\}$을 표현하는 Grammer의 뒤에 b하나만 추가하면 된다.
- $P:$
	- $S → Ab$ `여기서 b 추가함`
	- $A → aAb| λ$ `기존 Grammer`

하나의 Languague를 표현하는 서로 다른 여러 Grammer가 있을 수 있다.
# Deterministic Finite Accepters (DFA)
$M = (Q,Σ,δ,q_{0},F)$
- $Q$ - Set of States
- $Σ$ - Input alphabet
- $δ$ - $Q \times Σ → Q$ (Transition Function)
- $q_{0}$ - Initial State
- $F ⊂ Q$ - Final States

## Example
$M = (\{q_{0},q_{1},q_{2}\},\{0,1\},δ, q_{0}\{q_{1}\})$
- $δ(q_{n},t) = q_{m}$
	- $t$ is terminal
- $δ^{*}(q_{0},w)=q_{2}$
	- $w$ is string

| Init  | transition | Destination |
| ----- | ---------- | ----------- |
| $q_0$ | 0          | $q_0$       |
| $q_0$ | 1          | $q_1$       |
| $q_1$ | 0          | $q_0$       |
| $q_1$ | 1          | $q_2$       |
| $q_2$ | 0          | $q_2$       |
| $q_2$ | 1          | $q_1$       |
- 모든 Input alphabet 에 대한 Edge가 존재해야 함.
- Input alphabet에 따라 다음 State가 하나로 결정되기 때문에 **deterministic** 하다.
- State가 유한한 갯수이므로 **Finite** 하다.
- 특정 String을 Accept/Unaccept 하기 때문에 **Accepter**이다.
### DFA Transition Graph

 ```dot
digraph G {
	rankdir = LR
    // Set default node style
    node [shape=circle, style=filled, color=black, fillcolor=white];

    // Define nodes
    q0;
    q1 [shape=doublecircle];
    q2;

    // Define edges with labels
    start [shape=point, style=invisible];

	start -> q0
    q0 -> q1 [label="1"];
    q1 -> q0 [label="0"];
    q0 -> q0 [label="0"];
    q1 -> q2 [label="1"];
    q2 -> q1 [label="1"];
	q2 -> q2 [label="0"];
}

```

## Define Language By DFA
$L(M) = \{w ∈ Σ^{*}:δ^{*}(g_{0},w)∈F\}$
## 특정 문자열을 받아들이는/받아들이지 않는 DFA 만들기
001을 받아들이지 않는 문자열 만들기
### **Step 1.** 001을 받아들이는 DFA를 만든다.
 ```dot 
 digraph G { rankdir = LR node [shape=circle, style=filled, color=black, fillcolor=white]; 
 q0; 
 q1;
 q2; 
 q3 [shape=doublecircle]; 
 start [shape=point, style=invisible]; 
 
 start -> q0; 
 q0 -> q1 [label="0"]; 
 q1 -> q2 [label="0"]; 
 q2 -> q3 [label="1"]; 
 } 
 ```
### **Step 2.** DFA 요건을 만족시킨다.
```dot 
 digraph G { rankdir = LR node [shape=circle, style=filled, color=black, fillcolor=white]; 
 q0; 
 q1;
 q2; 
 q3 [shape=doublecircle]; 
 start [shape=point, style=invisible]; 
 
 start -> q0; 
 q0 -> q1 [label="0"]; 
 q0 -> q0 [label="1"]; 
 q1 -> q2 [label="0"];
 q1 -> q0 [label="1"];
 q2 -> q3 [label="1"];
 q2 -> q2 [label="0"];
 q3 -> q3 [label="0,1"] 
 }
 ```
### **Step 3.** Final을 Non-Final로 바꾼다.
```dot 
 digraph G { rankdir = LR node [shape=circle, style=filled, color=black, fillcolor=white]; 
 q0[shape=doublecircle]; 
 q1[shape=doublecircle];
 q2[shape=doublecircle]; 
 q3 ; 
 start [shape=point, style=invisible]; 
 
 start -> q0; 
 q0 -> q1 [label="0"]; 
 q0 -> q0 [label="1"]; 
 q1 -> q2 [label="0"];
 q1 -> q0 [label="1"];
 q2 -> q3 [label="1"];
 q2 -> q2 [label="0"];
 q3 -> q3 [label="0,1"] 
 }
 ```
 
# Regular Languages 
$L$을 Accept하는 DFA가 있다면, $L$은 Regular 하다.
즉 $L$이 Regular 한지 아닌지 파악하기 위해, $L$의 DFA를 만들어 보면 알 수 있다.
# Non-Deterministic Finite Accepters
$M = (Q,Σ,δ,F,q_{0})$

# Equivalence of Deterministic and Nondeterministic Finite Accepter
# Reduction of the Number of States in Finite Automata
# Reduction of the Number of States in Finite Automata & Regular Expressions
# Regular Expressions & Connection Between Regular Expressions and Regular Languages
# Connection Between Regular Expressions and Regular Languages
# Regular Grammars
# Closure Properties of Regular Languages
# Closure Properties of Regular Languages and Elementary Questions about Regular Languages
# Identifying Nonregular Languages Part 1
# Identifying Nonregular Languages

$L = \{a^{n}b^{n}:n\ge 0\}$
- $a^{m}b^{m}$
	- $|y| = k$ 일 때, $a^{m-k}b^{m}$ 역시 $L$에 포함되어야 한다.
	- 그러나 $a^{m-k}b^{m} ∉ L$, 따라서 가정은 모순이다.
	- $L$ is not regular.
$L = \{ww^{R}:w ∈ Σ^{*}\}$
- $a^{m}b^{m}b^{m}a^{m}$
	- $|y| = k$ 일 때, $a^{m-k}b^{m}b^{m}a^{m}$ 역시 $L$에 포함되어야 한다.
	- 그러나 $a^{m-k}b^{m}b^{m}a^{m} ∉ L$, 따라서 가정은 모순이다.
	- $L$ is not regular.
$L = \{w ∈ Σ^{*}: n_{a}(w)<n_{b}(w)\}$
- $a^{m}b^{m+1}$
	-  $|y| = k$ 일 때, $a^{m+k}b^{m+1}$ 역시 $L$에 포함되어야 한다.
	- $m+k \ge m+1$
		- 따라서 가정은 모순이다.
	- $L$ is not regular
$L = \{(ab)^{n}a^{k}:n>k, k \ge 0\}$
- $(ab)^{m+1}a^{m}$
	- $|y| = k$ 일 때, $(ab)^{m+1-k}a^{m}$ 역시 $L$에 포함되어야 한다.
		- $y$에 $b$가 포함된 경우 - 모순
		- $y$에 $b$가 포함되지 않은 경우 - 패턴 생성 불가능, 모순
	- $L$ is not regular

> pumping lemma를 적용하는 위치는 반드시 string의 앞일 필요는 없다.
> string의 중간, 끝에서도 사용할 수 있다.

$L = \{a^{n^{2}}: n \ge 0\}$
- $a^{m^{2}}$
	- $|y| = k \le m$일 때, $a^{m^{2}}+k$ 역시 $L$에 포함되어야 한다.
		-  $a^{m^{2}}+k \ne a^{(m+1)^2}$
	- 이는 불가능하므로, 가정은 모순이다.
	- $L$ is not regular

$L = \{a^{n}b^{k}c^{n+k}:n \ge 0, k \ge 0\}$
- $a^{m}b^{m}c^{2m}$
	- $|y| = k$일 때,  $a^{m-k}b^{m}c^{2m}$역시 $L$에 포함되어야 한다.
		- $2m-k \le 2m$
	- 이는 불가능하므로, 가정은 모순이다.
	- $L$ is not regular
## Homomorphism을 이용한 증명

$h(a) = a$
$h(b) = a$
$h(c) = c$
$Γ(L) = \{a^{n+k}c^{n+k}:n \ge 0, k \ge 0\}$

Homomorphism은 Regular language에 의해 닫혀있으므로,
$Γ(L)$ 이 regular 하지 않으면, $L$ 역시 regular 하지 않다.
## Complement를 이용한 증명

$L = \{a^{n}b^{l} : n \ne l\}$
$L' = \{a^{n}b^{l}:n=l\}$ . which is not regular.

$L ∪ L' = L(a^{*}b^{*})$
- $\overline{L} ∩ L(a^{*}b^{*}) = L'$
	- Since every operation we did is closed in regular languages, 
		- $L$ is regular when $L'$ is regular.
- But $L'$ is not regular, so $L$ is also not regular.
---
# Context-free Grammer
> Stronger grammer than regular grammer

$G = (\{S\}, \{a,b\}, S, P)$
- $S → aSa$
- $S → bSb$
- $S → λ$
- Why it is context free? : 문맥에 영향을 받지 않기 때문.
	- 왼쪽에 Terminal이 나오지 않는다.
- Derivation
	- $S ⇒ aSa ⇒ aaSaa ⇒ aabSbaa ⇒ aabbaa$
- Context free language로는 Regular Language가 표현하지 못하는 언어도 표현할 수 있다.

Opposite : **Context-sensitive**
- $aSb → acb$
- $aSc → adc$

### **다음 Grammer가 만드는 Language는?**
$S → abB$
$A → aaBb$
$B → bbAa$
$A → λ$

- $B → bba|bbaaBba$
	- $\{L = ab(bbaa)^{n}bba(ba)^{n}, n \ge 0\}$

### **다음 Language가 만드는 Grammer는?**
$L = \{a^{n}b^{m}:n \ne m\}$
> a의 갯수와 b의 갯수가 다르다는 것은, a의 갯수보다 b의 갯수가 많거나, 혹은 그 반대를 의미한다.

**Step 1) a의 갯수가 b의 갯수보다 많은 Language를 생성하는 Grammer**
$S → AS_{1}$
$S_{1}→ aS_{1}b| λ$
$A → aA|a$

**Step 2) 이를 바탕으로, a의 갯수와 b의 갯수가 서로 다른 Language를 생성하는 Grammer**
$S →AS_{1}|S_{1}B$
$S_{1}→aS_{1}b| λ$
$A → aA|a$
$B → bB|b$

### **다음 Grammer가 생성하는 Language는?**
$S → aSb|SS| λ |bSa$
> a의 갯수와 b의 갯수가 같은 Language.

**만약 이 Grammer를 다음과 같이 바꾼다면?**
$S → aSb|SS| λ$

**이 Grammer는 아래의 string을 만들 수 없다.**
- $ba$ (b로 시작하는 모든 language) 
- $abba$ 

**정의하자면,**
$n_{a}(w) = n_{b}(w)$ `1. a의 갯수와 b의 갯수가 같다.`
$n_{a}(v) \ge n_{b}(v)$  `3. a의 갯수가 항상 b의 갯수보다 크거나 같다.`
$v$ is any prefix of $w$ `2. 이 Grammer로 인해 만들어지는 String의 prefix v는`

만약 $a$가 열린 괄호, $b$가 닫힌 괄호라면, 이 Grammer은 legal 한 괄호 표현법을 나타내는 Grammer이다.

#### 증명
$aSb$ 가 terminal을 생성하는 유일한 문법이므로, $a$와 $b$의 갯수는 항상 같다 (`1.`) 
$a$가 항상 $b$보다 먼저 나오기 때문에, Prefix항상 $a$의 갯수가 많다. (`2.3.`)

## **Left-most Derivation and Right-most Derivation**

특정 Grammer가 주어졌을 때, Sentential form의 왼쪽 Variable부터 순차적으로 Derivation 하는 것을 
**Left-most Derivation**이라고 한다.

### *Example*
$S → aAB$
$A → bBb$
$B →A| λ$

Derivation (**Left-Most**)
$S ⇒ aAB ⇒ abBbB ⇒abAbB ⇒ abbBbbB ⇒abbbbB ⇒ abbbb$

Derivation (**Right-Most**)
$S ⇒ aAB ⇒ aAA ⇒ abBbA ⇒ abBbbBb ⇒ abBbbb ⇒ abbbb$

같은 String을 Derivation 하는 **여러가지 Derivation** 방법이 있다.

## Parse Tree
Derivation 과정을 Child Node로 표현한 것
### $S ⇒ aAB ⇒ abBbB ⇒ abBbA ⇒ abbBbbBb ⇒ abbbBb ⇒ abbbb$

```mermaid
graph TD
	1((S));
	2((a));
	3((A));
	4((b));
	5((B));
	6((λ));
	7((b));
	8((B));
	9((A));
	10((b));
	11((B));
	12((λ));
	13((b));

	1 --> 2
	1 --> 3
	3 --> 4
	3 --> 5
	5 --> 6
	3 --> 7
	1 --> 8
	8 --> 9
	9 --> 10
	9 --> 11
	11 --> 12
	9 --> 13
```

> Parse Tree 역시 derivation의 갯수 많큼 많아질 수 있다.
### $S ⇒ aAB ⇒ abBbB ⇒abAbB ⇒ abbBbbB ⇒abbbbB ⇒ abbbb$

```mermaid
graph TD
	1((S));
	2((a));
	3((A));
	4((B));
	
	5((b));
	6((B));
	7((b));
	
	9((A));
	
	10((b));
	11((B));
	12((b));
	13((λ));
	14((λ));

	1 --> 2
	1 --> 3
	1 --> 4
	
	3 --> 5
	3 --> 6
	3 --> 7
	6 --> 9

	9 --> 10
	9 --> 11
	9 --> 12

	4 --> 13
	11 --> 14
```

> Parse Tree 가 여러개 나오면 그 문법을 Ambiguous 하다고 하는데, 이는 지양해야 한다.



















