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

Opposite : Context-sensitive
- $aSb → acb$
- $aSc → adc$

다음 Grammer가 만드는 Language는?
$S → abB$
$A → aaBb$
$B → bbAa$
$A → λ$

$\{L = ab(bb)^{n}bba(ba)^{n}, n \ge 0\}$


$L = \{a^{n}b^{m}:n \ne m\}$

