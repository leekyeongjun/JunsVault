

```mermaid
stateDiagram-v2
direction LR

classDef f font-weight:bold,stroke-width:4px,stroke:black

[*] --> q0
state q0
q0 --> q1 : a
state q1
q1 --> q2 : b 
state q2

class q1 f
class q2 f
```
