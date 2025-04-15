

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