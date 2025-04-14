# Composing transformations (transformation 합치기)
- 특정 point $p$에 $T$ 라는 조작 뒤에 $S$ 라는 조작이 뒷 따랐다고 가정하자.
	- $p \to T(p) \to S(T(P)) = (S \circ T)(p)$
	- 이때를 S compose T라고 표현한다.
- $T(p) = M_{T}p;\ S(p) = M_{S}p$
	- $(S \circ T)(p) = M_{S}M_{T}p$
		- T다음에 S가 오는거임.
		- Composing에서는 결합법칙은 성립해도, 교환법칙은 성립하지 않음.
		- ![[Pasted image 20240401221541.png]]
# Composing Translations (Translation 합치기)
- $p \to T(p) \to S(T(P)) = (S \circ T)(p)$
	- $T(p) = p+u_{T}, S(p) = p+u_{S}$
		- $(S \circ T)(p) = p+ (u_{T}+u_{S})$
	-  Translation은 교환법칙이 성립함.

# Composion Affine transformation
- $T(p) = M_{T}p+u_{T}, S(p) = M_{S}p+u_{S}$
	- $(S \circ T)(p) = M_{S}(M_{T}p + u_{T})+u_{S} = (M_{S}M_{T})p + (M_{S}u_{T})+u_{S}$
- 되긴 할텐데... 조금 어색할거임. 계산하기도 힘들고...
- 그래서 등장한 개념이 Homogeneous coordinates.
# Homogeneous coordinates
- 2D point를 3개의 숫자를 사용해 표현하자.
- w 라는 별도의 컴포넌트를 사용해 벡터를 표현하고, 여분의 row/column은 행렬하는데 쓴다.
	- point에 한해 w는 항상 1이다.
	- $[x,y]^{t}$ 였던 2D point를 $[x,y,1]^{t}$로 변형 (신규 데이터추가)
	- Linear transform의 변화
	- $$
\begin{bmatrix} 
a && b && 0\\
c && d && 0\\
0 && 0 && 1\\
\end{bmatrix}
\begin{bmatrix} 
x \\
y \\
1 \\
\end{bmatrix} = 
\begin{bmatrix} 
ax+by\\
cx+dy\\
1
\end{bmatrix}
$$
- 좌표에 신규 데이터 w 가 추가됨으로써, Trasformation matrix도 3x3으로 확장됨.
	- Translation - 좌측
	- $$
\begin{bmatrix} 
1 && 0 && t(x\ translation)\\
0 && 1 && s(y\ translation)\\
0 && 0 && 1\\
\end{bmatrix}
\begin{bmatrix} 
x \\
y \\
1 \\
\end{bmatrix} = 
\begin{bmatrix} 
x+t\\
y+s\\
1
\end{bmatrix}
$$
	- Linear - 안쪽
- 정리하자면...
좌표 $(x,y)$를 x축 기준으로 $a$배, y축 기준으로 $b$ 배 Scale 한 뒤, x 축 기준으로 $c$ 만큼, y축 기준으로 $d$ 만큼 Shear 한뒤, x축기준으로 $e$ 만큼, y축 기준으로 $f$ 만큼 translate 한 행렬은...
$$
\begin{bmatrix} 
a && c && e \\
d && b && f \\
0 && 0 && 1 
\end{bmatrix}
\begin{bmatrix} 
x \\ y \\ 1
\end{bmatrix}
$$