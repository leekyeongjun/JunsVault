# What is Transformation?
- "moving a set of points"
- Translate, Rotate, Scale, Shear, Reflect...

# Points and Vectors
- point $\dot p$ is a place in space
- vector $\vec{v}$ 는 position 과 독립적인 방향을 묘사할 때 사용

# Notations
- 3D Euclidean Vector
$$
\vec{v} = 
\begin{bmatrix}
a \\ b \\ c 
\end{bmatrix}
= [a\ b\ c]^{t}
$$
![[Pasted image 20240401195346.png]]

# Vector Calculation
- $\vec{u} = [1,3,7]^{t}$ , $\vec{v} = [2,2,-3]^{t}$,  $\vec{w} = [3,5,4]^{t}$
	- $\vec{u}+\vec{v} = [(1+2),(3+2),(7+(-3))]^{t} = \vec{w}$
	- $2\vec{u} = [(1\cdot2),(3\cdot7),(7\cdot2)]^{t} =[2,6,14]^{t}$
	- $-\vec{v} = [-2,-2,3]^{t}$

# Transforming Geometry
- 집합 내의 모든 point/vector에 대하여 동일한 연산을 수행하는 것.
- $S \to \{T(v)\ |\ v\in S\}$
	- S : 벡터 공간
	- T는 Transformation
		- 즉, Transforming이란 벡터 공간 안에 있는 모든 벡터 V에 대해 동일한 조작 T를 가하는 것.

## Linear transformations
- transformation을 정의 할 수 있는 하나의 방법은 "행렬의 곱셈"이다.
- $T(v) = Mv$
	- $T(au+v)=aT(u)+T(v)$ (선형)
- 모든 Linear Transformation은 다음과 같이 쓰여질 수 있다.

## Methods
### 1. Uniform Scale (균일한 Scaling)
$$
\begin{bmatrix} 
s && 0\\
0 && s
\end{bmatrix}
\begin{bmatrix} 
x\\
y
\end{bmatrix} = 
\begin{bmatrix} 
sx+0y \\
0x+sy 
\end{bmatrix} =
\begin{bmatrix} 
sx \\
sy 
\end{bmatrix}
$$
(x는 x축으로 s배, y는 y축으로 s배)
타겟 point의 좌표가 (x,y)
#### 행렬의 곱셈
![[Pasted image 20240401200957.png]]
- 행하나 잡고, 곱셈 행렬의 각 element를 구성한다고 생각하자.
![[Pasted image 20240401202146.png]]
- 3x3
$$
\begin{bmatrix} 
a && b && c\\
d && e && f\\
g && h && i\\
\end{bmatrix}
\begin{bmatrix} 
j && k && l\\
m && n && o\\
p && q && r\\
\end{bmatrix} = 
\begin{bmatrix} 
aj+bm+cp && ak+bn+cq && al+bo+cr\\
dj+em+fp && dk+en+fq && dl+eo+fr\\
gj+hm+ip && gk +hn +iq && gl+ho+ir\\
\end{bmatrix}
$$
### 2. Nonuniform scale
$$
\begin{bmatrix} 
s_{x} && 0\\
0 && s_{y}
\end{bmatrix}
\begin{bmatrix} 
x\\
y
\end{bmatrix} = 
\begin{bmatrix} 
s_{x}x+0y \\
0x+s_{y}y 
\end{bmatrix} =
\begin{bmatrix} 
s_{x}x \\
s_{y}y 
\end{bmatrix}
$$
### 3. Rotation
![[Pasted image 20240401215248.png]]
- 회전 행렬 $R_{\theta} = \begin{bmatrix} \cos\theta && -\sin\theta \\ \sin\theta && \cos\theta \end{bmatrix}$

$$
\begin{bmatrix} 
\cos\theta && -\sin\theta 
\\ \sin\theta && \cos\theta
\end{bmatrix}
\begin{bmatrix} 
x\\
y
\end{bmatrix} = 
\begin{bmatrix} 
x\cos\theta -y\sin\theta \\
x\sin\theta + y\cos\theta
\end{bmatrix}
$$
### 4. Reflection
$$
\begin{bmatrix} 
s && 0\\
0 && s
\end{bmatrix}
\begin{bmatrix} 
-1 && 0\\
0 && 1
\end{bmatrix} = 
\begin{bmatrix} 
-s+0 && 0 \\
0 && 0+s 
\end{bmatrix} =
\begin{bmatrix} 
-s && 0 \\
0 && s 
\end{bmatrix}
$$
- nonuniform scale의 특수한 사례로 봐도 됨.
### 5. Shear
$$
\begin{bmatrix} 
s && a\\
0 && s
\end{bmatrix}
\begin{bmatrix} 
x\\
y
\end{bmatrix} = 
\begin{bmatrix} 
sx+ay \\
sy
\end{bmatrix}
$$
### 6. Translation
$$
\begin{align*}
T(v) = v+u \\
T^{-1}(v) = v-u
\end{align*}
$$
- Linear Transformation? - No.
- It's not about matrix multiplication, but about "Addition".
- Combining with linear transformation
	- $T(v) = Mv+u$ "Affine transformation"
