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
# Non-Deterministic Finite Accepter
$M = (Q,Σ,δ,F,q_{0})$
$δ : Q \times (Σ ∪ \{λ\}) → 2^{Q}$

- Input alphabet이 없어도 **(λ) Transition** 가능.
	- λ transition은 해도 되고 안해도 됨. 즉 2가지의 경우가 있을 수 있음.
- 하나의 Input alphabet에 대하여 여러개의 State로 갈 수 있음.
	- DFA와는 달리, NFA는 도달하는 모든 집합 중 하나라도 Final State가 존재하면 됨.
# NFA → DFA
- 모든 Terminal에 대하여 Transition을 행한 테이블을 바탕으로 변환함.
```dot 
 digraph G { rankdir = LR node [shape=circle, style=filled, color=black, fillcolor=white]; 
 q0; 
 q1 [shape=doublecircle];
 q2;  
 start [shape=point, style=invisible]; 
 
 start -> q0; 
 q0 -> q1 [label="0,1"]; 
 q0 -> q0 [label="0"]; 
 q1 -> q2 [label="0,1"];
 q2 -> q2 [label="1"];
 }
 ```
 - Transition Detail (Term)
	 - λ* Term
	 - Term λ*
	 - Term
	 - λ* Term λ*
 - Transition Detail (λ)
	 - Do λ transtion
	 - Do not λ transtion


| 출발    | Term | λ T | T λ | T     | λ T λ | Result |
| ----- | ---- | --- | --- | ----- | ----- | ------ |
| 0     | 0    | ∅   | ∅   | 0,1   | ∅     | 0,1    |
|       | 1    | ∅   | ∅   | 1     | ∅     | 1      |
| 0,1   | 0    | ∅   | ∅   | 0,1,2 | ∅     | 0,1,2  |
|       | 1    | ∅   | ∅   | 1,2   | ∅     | 1,2    |
| 1     | 0    | ∅   | ∅   | 2     | ∅     | 2      |
|       | 1    | ∅   | ∅   | 2     | ∅     | 2      |
| 0,1,2 | 0    | ∅   | ∅   | 0,1,2 | ∅     | 0,1,2  |
|       | 1    | ∅   | ∅   | 1,2   | ∅     | 1,2    |
| 1,2   | 0    | ∅   | ∅   | 2     | ∅     | 2      |
|       | 1    | ∅   | ∅   | 2     | ∅     | 2      |
| 2     | 0    | ∅   | ∅   | ∅     | ∅     | ∅      |
|       | 1    | ∅   | ∅   | 2     | ∅     | 2      |
```dot 
digraph G { rankdir = LR; node [shape=circle, style=filled, color=black, fillcolor=white]; 
	q0; 
	q01[shape=doublecircle];
	q1[shape=doublecircle];
	q012[shape=doublecircle];
	q12[shape=doublecircle];
	q2;  
	T;
	
	start [shape=point, style=invisible]; 
	
	start -> q0; 
	q0 -> q01 [label="0"]; 
	q0 -> q1 [label="1"]; 
	q01 -> q12 [label="1"];
	q01 -> q012 [label="0"];
	q1 -> q2 [label="0,1"];
	q012 -> q012 [label="0"];
	q012 -> q12 [label="1"];
	q12 -> q2 [label="0,1"];
	q2 -> T [label="0"];
	q2 -> q2 [label="1"];
	T -> T [label="0,1"];
}
 ```

# DFA State 갯수 줄이기
## Step 1. Initial State에서 도달 불가능한 State 삭제
## Step 2. Distinguishable / Indistinguishable
- Indistinguishable
	- $δ^{*}(p,w)∈F , δ^{*}(q,w) ∈F$
	- $δ^{*}(p,w) ∉ F, δ^{*}(q,w) ∉F$
### String의 길이를 늘려가며 Distinguishableness 확인
```dot 
digraph G { rankdir = LR; node [shape=circle, style=filled, color=black, fillcolor=white]; 
	0; 
	1;
	2 [shape=doublecircle];
	3;
	4 [shape=doublecircle];
	
	start [shape=point, style=invisible]; 
	
	start -> 0;
	0 -> 1 [label="0"];
	0 -> 3 [label="1"];
	1 -> 2 [label="0"];
	2 -> 1 [label="0"];
	3 -> 2 [label="0"];
	1 -> 4 [label="1"];
	2 -> 4 [label="1"];
	3 -> 4 [label="1"];
	4 -> 4 [label="0,1"];
}
 ```
 

| String length | string | Division          |
| ------------- | ------ | ----------------- |
| 0             | λ      | 0,1,3 2,4         |
| 1             | 0      | 0 1,3 2 4         |
|               | 1      | 0 1,3 2 4. Finish |
|               |        |                   |

```dot 
digraph G { rankdir = LR; node [shape=circle, style=filled, color=black, fillcolor=white]; 
	0; 
	13;
	2 [shape=doublecircle];
	4 [shape=doublecircle];
	
	start [shape=point, style=invisible]; 
	
	start -> 0;
	0 -> 13 [label="0,1"];
	13 -> 2 [label="0"];
	2 -> 13 [label="0"];
	13 -> 4 [label="1"];
	2 -> 4 [label="1"];
	4 -> 4 [label="0,1"];
}
 ```
## Example
 ```dot 
digraph G { rankdir = LR; node [shape=circle, style=filled, color=black, fillcolor=white]; 
	0; 
	1;
	2;
	3 [shape=doublecircle];
	4;
	5 [shape=doublecircle];
	
	start [shape=point, style=invisible]; 
	
	start -> 0;
	0 -> 1 [label="0"];
	1 -> 0 [label="0"];
	2 -> 1 [label="0"];
	2 -> 4 [label="1"];
	4 -> 3 [label="0,1"];
	1 -> 3 [label="1"];
	0 -> 3 [label="1"];
	3 -> 5 [label="0,1"];
	5 -> 5 [label="0,1"];
}
 ```
 - Step 1. Initial State에서 도달 불가능한 State 삭제
	 - 2, 4 삭제
```dot 
digraph G { rankdir = LR; node [shape=circle, style=filled, color=black, fillcolor=white]; 
	0; 
	1;
	3 [shape=doublecircle];
	5 [shape=doublecircle];
	
	start [shape=point, style=invisible]; 
	
	start -> 0;
	0 -> 1 [label="0"];
	1 -> 0 [label="0"];
	1 -> 3 [label="1"];
	0 -> 3 [label="1"];
	3 -> 5 [label="0,1"];
	5 -> 5 [label="0,1"];
}
 ```
 - Step 2. Distness 확인

| String length | string | Division      |
| ------------- | ------ | ------------- |
| 0             | λ      | 0,1 3,5       |
| 1             | 0      | NN, FF        |
|               | 1      | FF, FF, Stop. |
