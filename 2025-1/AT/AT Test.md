

```mermaid
stateDiagram-v2
direction LR

classDef f font-weight:bold,stroke-width:4px,stroke:black

[*] --> q0
state q0
q0 --> q1 : λ
state q1
q1 --> q2 : δ
state q2

class q1 f
class q2 f
```
```mehrmaid
```
```mehrmaid

```

```mehrmaid
flowchart LR

A --> C
B --> D
C & D --> E
E --> F & G
F --> H
G --> J
A -.->|"$Σ^{*}$"|A
A-->|text|B


A((("$A$")))
G(("$G$"))
E(("$E$"))
C(("$C$"))
D(("$D$"))
F(("$F$"))
B(("$B$"))
H(("$H$"))
J(("$J$"))

```

```dot
digraph G 
{ subgraph cluster_0 { style=filled; color=lightgrey; node [style=filled,color=white]; a0 -> a1 -> a2 -> a3; label = "process #1"; } subgraph cluster_1 { node [style=filled]; b0 -> b1 -> b2 -> b3; label = "process #2"; color=blue } start -> a0; start -> b0; a1 -> b3; b2 -> a3; a3 -> a0; a3 -> end; b3 -> end; start [shape=Mdiamond]; end [shape=Msquare]; }

```
