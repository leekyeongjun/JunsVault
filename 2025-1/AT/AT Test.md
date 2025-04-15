

```mermaid
stateDiagram-v2
direction LR
[*] --> Still
Still --> [*]

Still --> Moving
Moving --> Still
Moving --> Crash
Crash --> [*]
```
