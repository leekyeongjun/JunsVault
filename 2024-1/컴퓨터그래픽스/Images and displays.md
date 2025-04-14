- What is an Image?
	- Bitmap graphic
		- jpg, png, jpec
	- Vector graphic
		- svg, pdf, eps
# Image is...
- 색상의 강도에 대한 2D 표현
- 2차원 Plane에 대한 함수로 표현
$$
I = R^{2} \to \ ...
$$
# Display Technologies
- Computer displays
	- CRT 모니터
	- LCD 모니터
	- OLED, DLP ...
- 프린터
	- 레이저 프린터
	- 잉크젯 프린터 

## Color Displays
- 인간의 눈은 3원색을 기준으로 본다.
- 따라서 위 세가지의 Color Type을 바탕으로 색을 나타내면 된다.
![[Pasted image 20240311175830.png]]
- R : (1,0,0)
- G : (0,1,0)
- B : (0,0,1)

## Electroning paper
- Epaper는 스스로 빛을 내는게 아니라, 기존 빛을 반사하기 때문에, 매우 밝은 환경에서는 다른 디스플레이보다 우월한 성능을 낸다.

# Raster Image를 위한 Datatype
## Bitmaps
- 한 픽셀을 Boolean으로 표현 (1bpp (bit per pixel))
- 주로 흑백의 이미지 (글자 등)
-  $I : R^{2} \to \{0,1\}$
## Grayscale
- 한 픽셀을 Integer로 표현 (8bpp, 10,12,16...)
- E -Ink (4bpp)
- $I : R^{2} \to [0,1]$
## Color
- 모든 시각화 가능한 색상을 씀
- 대체로 한 픽셀을 3개의 바이트의 배열로 표현 (24bpp)
- 간혹 16,30,36,48 bpp(HDR) 일 때도 있음
- indexed color : 컬러 팔레트를 쓰는 방식이지만, 사장되어 가고 있음
- $I : R^{2}\to [0,1]^{3}$
## Floating point
- RGBA (Alpha) 채널도 사용
- 한 픽셀을 4개의 바이트 배열로 표현 (32bpp)
- 그래픽 프로세서의 표준
- $I : R^{2}\to R_{+}\ or\ I:R^{2}\to R^{3}_{+}$ 

### Raw Image를 위한 저장 공간
- 1024 X 1024 이미지라면...
	- 비트맵 : 128 KB
	- Grayscale 8bpp : 1MB
	- Grayscale 16bpp : 2MB
	- color 24bpp : 3MB
	- floating point HDR color : 12MB

### 픽셀 포맷간의 변환
- color to gray
	- 하나의 채널 만을 사용하는 법 (Blue 채널 같은 걸로...) 
		- Gray 값을 위해 이상한 값을 가져올 수 있음
	- Channel 간의 결합이 더 나음
		- Gray = 0.2R + 0.7G +0. 1B
- Pixel Precision에 의해 Converting 하는 방법
	- bpp를 줄이는 건 정보를 상실시킬수 있으므로 주의해야 한다.
	- Dithering
		- Inconsistent Quantization 기법
		- 한 픽셀 주변의 픽셀을 파악해서 일정하지 않게 dithering 하는 법
			- Ordered (Laser)
			- Diffusion (Inkjet)
	- Gamma correction
		- 너무 낮거나 높은 감마값을 바꿔주는 것