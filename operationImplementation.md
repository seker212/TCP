ID (dec) | Op. Description | Sender | Recever check | Answer sending | Answer check
---|---|---|---|---|---
0 |  set sessionID  | [ ] Server | [x] Client
1 |  set name |  [x] Client | [ ] Server | [ ] Server | [x] Client
2 |  new client available |  [ ] Server | [ ] Client
3 | search for other client | [x] Client |  [ ] Server | [ ] Server | [ ] Client
4 | no other clients available |  [ ] Server | [ ] Client
5 | send room invitation |  [x] Client |  [ ] Server | [ ] Server | [ ] Client
6 | forward room invitation |  [ ] Server | [ ] Client
7 | accept room invitation |  [x] Client |  [ ] Server | [ ] Server | [ ] Client
8 | refuse room invitation |  [x] Client |  [ ] Server | [ ] Server | [ ] Client
9 | client has joined the room |  [ ] Server | [ ] Client
10 | client has left the room |  [ ] Server | [ ] Client
11 | send message | [ ] Client |  [ ] Server | [ ] Server | [ ] Client
12 | broadcast message | [ ] Server | [ ] Client
13 | leave room | [x] Client |  [ ] Server | [ ] Server | [ ] Client
14 | disconnect | [x] Client |  [ ] Server | [ ] Server | [ ] Client
15 | ERROR | [ ] Client |
15 | ERROR| [ ] Server |