## Matching Engine

A central registry to allow users to find, join, and leave lobbies.

The schema for the message types that the matching engine sends and receives is available in `../lobby-message-codecs`.

```mermaid
flowchart LR
    subgraph Domain Logic 
        h1
        h2
        h3
    end
    I[IngressProcessor] --> h1[Handler]
    I --> h2[Handler]
    I --> h3[Handler]
    h1 --> CR[ClientResponder]
    h2 --> CR
    h3 --> CR
```
