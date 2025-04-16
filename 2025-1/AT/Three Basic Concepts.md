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