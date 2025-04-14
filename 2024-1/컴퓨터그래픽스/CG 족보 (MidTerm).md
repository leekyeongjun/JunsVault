# 유형 1 - 이미지 크기
1. How many bytes are necessary to store a 1024×1024 color image with an alpha channel using 8 bits per channel? (2013)
   ![[Pasted image 20240428230759.png]]
   
2. How many bytes are necessary to store a 1024×1024 color image without an alpha channel using 8 bits per channel? (2017)
   
   ![[Pasted image 20240428230801.png]]
3. How many bytes are necessary to store a 1024 × 1024 color image without an alpha channel using 8 bits per channel? (2023)

![[Pasted image 20240428230802.png]]
# 유형 2 - Implicit Equation
1. What is the implicit equation of the plane through 3D points (1,0,0), (0,1,0), and (0,0,1)? What is the parametric equation? What is the normal vector to this plane?

![[Pasted image 20240428230804.png]]


2. What is the equation of the plane through 3D points (-1,0,0), (0,1,0), and (0,0,1)? What is the normal vector to this plane?

![[Pasted image 20240428230805.png]]


3. What is the equation of the plane through 3D points p1, p2, p3 ? What is the normal vector to this plane?


![[Pasted image 20240428230806.png]]

4. What is the equation of the plane through 3D points a,b ,and c ?

![[Pasted image 20240428230807.png]]


5. What is the equation of the plane through 3D points a, b, and c? What is the normal vector to this plane? You can use any vector operators in the answer.

![[Pasted image 20240428230808.png]]


6. What is the equation of the plane through 3D points a, b, and c? What is the normal vector to this plane? You can use any vector operators in the answer.
![[Pasted image 20240428230809.png]]
# 유형3 - 반례
1. Show by counterexample that is is not always true that for 3D vectors a,b, and c, a×(b×c)=(a×b)×c .
   
   ![[Pasted image 20240428230810.png]]


# 유형4 - Ray-Sphere
1. What are the ray parameters of the intersection points between ray (1,1,1)+t(−1,−1,−1) and the sphere centered at the origin with radius 1?
   
   ![[Pasted image 20240428230811.png]]
   
   
2. What are the ray parameters of the intersection points between ray (1,1,1)+ t(−1,−1,−1) and a sphere centered at (−2,−2,−2) with radius 1?
   ![[Pasted image 20240428230812.png]]
   
   

3. What is the equation of the ray that starts from a toward c ? 


![[Pasted image 20240428230813.png]]


4. What are the ray parameters of the intersection points between ray a+t b and a sphere centered at c with radius 1?


![[Pasted image 20240428230815.png]]


5. What are the ray parameters of the intersection points between ray a+bt and a sphere centered at (0,0,0) with radius 2?
   ![[Pasted image 20240428230816.png]]
   
   
   
   
   
# 유형5 - Ray-Slab
1. Calculate the interval of the ray parameters of the intersection points between ray (1,1,1)+ t(−1,−1,−1) and a slab defined by two planes shown below.
   ![[Pasted image 20240428212044.png]]
   
   
   
   ![[Pasted image 20240428230818.png]]
   
   
   
2. Calculate the ray parameter of the intersection points between ray (−1,−1,1)+t(−1,−1,−1) and an arbitrary plane ax+by+cz+d=0 (in terms of a, b, c, and d).
   
   
   ![[Pasted image 20240428230819.png]]
   
   
   

# 유형6 - Matrix Movement
1. Write down the 4×4 3D matrix to move by ( xm , ym ,zm ).
   
   ![[Pasted image 20240428230821.png]]
   
   
   
2. Write down the 4×4 3D matrix to rotate by an angle θ about the y-axis.
   ![[Pasted image 20240428230822.png]]
   
   
   
   
   
   
3. Write down the 4×4 3D matrix to rotate by an angle θ about the z-axis.

![[Pasted image 20240428230827.png]]





4. Write down the 4×4 rotation matrix M that maps the orthonormal 3D vectors u=(xu , yu ,zu ), v=( xv , yv ,z v ), and w=( xw , yw ,z w ), to orthonormal 3D vectors a=( xa , ya ,za ), b=( xb , yb ,zb ), and c=( xc , yc ,zc ), so M u=a , M v=b, and M w=c.


![[Pasted image 20240428230828.png]]





5. Describe in words what this 2D transformation matrix does: 
	1. $\begin{bmatrix} 0 && -1 && 1 \\ 1 && 0 && 1 \\ 0 && 0 && 1\end{bmatrix}$
	   
	   ![[Pasted image 20240428230835.png]]
	   
	2. $\begin{bmatrix} 3 && 0 && 2 \\ 0 && 3 && 4 \\ 0 && 0 && 1\end{bmatrix}$
	   
	   ![[Pasted image 20240428230836.png]]
	   
	3.  $\begin{bmatrix} 1 && 0 && 2 \\ 0 && 2 && 4 \\ 0 && 0 && 1\end{bmatrix}$
	
	  ![[Pasted image 20240428230837.png]]

	4. $\begin{bmatrix} 1 && 0 && 2 \\ 0 && -1 && 4 \\ 0 && 0 && 1\end{bmatrix}$
![[Pasted image 20240428230838.png]]
# 유형7 - top/geo
![[Pasted image 20240428213053.png]]
![[Pasted image 20240428230844.png]]
# 유형8 - The earth
![[Pasted image 20240428213121.png]]
1.  Explain what per-vertex attributes need to be passed from the application to the vertex stage. 
   
   ![[Pasted image 20240428230845.png]]
   
   
2.  Describe the computations that need to be done at the vertex stage. 
   
   
   ![[Pasted image 20240428230846.png]]
   
3.  Explain what attributes are interpolated by the rasterizer for the fragment stage. 
   
   ![[Pasted image 20240428230847.png]]
   
   
4.  Describe the computations that need to be done at the fragment stage.
![[Pasted image 20240428230849.png]]
# 유형9 - 각도기
![[Pasted image 20240428213300.png]] ![[Pasted image 20240428213353.png]]
1. Represent vector c in terms of vector a and b using the dot product operator (·) and the length operator (| |).
   
   
   ![[Pasted image 20240428230851.png]]
   
   
2. Represent vector c and d in terms of vector a and b using the dot product operator (·) and the length operator (| |).
![[Pasted image 20240428230852.png]]






# 유형10 - 접시
![[Pasted image 20240428213537.png]]![[Pasted image 20240428213623.png]]
1. Represent vector c and d in terms of vector a and b using the cross product operator (×) and the length operator (| |). Vector a , b, c are in the same plane, and d is orthogonal to the other vectors. All vectors have unit length.
   
   
   ![[Pasted image 20240428230855.png]]
   
   
2. Briefly explain why || d || == || c || when d=a×b and || a ||=1.
![[Pasted image 20240428230856.png]]



# 유형11 - 저장공간
1. Derive the average storage requirement (bytes per vertex) of the indexed triangle set representation assuming that a vertex contains a position and a normal (4byte float variables) and that the number of triangles is twice the number of vertices on average.




![[Pasted image 20240428230859.png]]
   
   
   
   
2. Derive the average storage requirement (bytes per vertex) of the indexed triangles representation assuming that a vertex contains a position, a 2D texture coord and a normal (all 4byte float variables) and that the number of triangles is twice the number of vertices on average.


![[Pasted image 20240428230901.png]]




# 유형12 - Projection matrix
![[Pasted image 20240428213906.png]]
1. Write down the 3×4 projection matrix that maps a 3d point (x,y,z) to (x',y')? Hint: similar triangles, homogeneous coordinates
   
   
   ![[Pasted image 20240428230903.png]]
   
   
2. Write down the 4 × 4 projection matrix that maps a 3d point (x,y,z) to (x',y',z)? Hint: similar triangles, homogeneous coordinates

![[Pasted image 20240428230905.png]]



# 유형13 Briefly explain~
1. Briefly explain why the measured dynamic range of the same display can differ depending on lighting conditions.
   
   ![[Pasted image 20240428230906.png]]
   
   
2. Briefly explain the main downside of the painter's algorithm, and then explain the alternative algorithm that is unanimously used in real-time applications such as games
![[Pasted image 20240428230907.png]]


3. Explain what the scale factor is, and why the scale factor is designed in such a way.

![[Pasted image 20240428230909.png]]
9.  Given two nonparallel, three-dimensional vectors u and v, how can we form an orthogonal coordinate system in which u is one of the basis vectors? Calculate all the basis vectors using the cross product operator (×) and the length operator (| |). 
	
![[Pasted image 20240428230911.png]]



- (b) Why is it important that the vectors u,v from (a) be non-parallel?
	  
	  
	  
	  ![[Pasted image 20240428230912.png]]
	  

# 유형14 Inverse
1. Given a rigid transformation matrix R of which the linear part Q is an orthonormal matrix, derive the inverse of the matrix R
	- $R=\begin{bmatrix} Q && u \\ 0 && 1\end{bmatrix}, R^{-1}$


![[Pasted image 20240428230918.png]]




2. Given a rigid transformation matrix R of which the linear part Q is an orthonormal matrix, verify that the inverse of orthormal matrix is as below.
	- $R=\begin{bmatrix} Q && u \\ 0 && 1\end{bmatrix}, R^{-1} = \begin{bmatrix} Q^{T} && -Q^{T}u \\ 0 && 1\end{bmatrix}$



![[Pasted image 20240428230919.png]]


