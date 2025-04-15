

```mermaid
stateDiagram-v2
direction LR

classDef f font-weight:bold,stroke-width:2px,stroke:black

[*] --> q0
q0 --> q1 : a
q1 --> q2 : b
q0 --> q1 : a

class q1 f
```
