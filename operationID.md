### Operation ID codes

ID (dec) | ID (hex) | ID (bin) | Description | Sender
--- | --- | --- | --- | ---
0 | 0 | 0000 | set sessionID | Server
1 | 1 | 0001 | set name | Client
2 | 2 | 0010 | new client available | Server
3 | 3 | 0011 | search for other client | Client
4 | 4 | 0100 | no other clients available | Server
5 | 5 | 0101 | send room invitation | Client
6 | 6 | 0110 | forward room invitation | Server
7 | 7 | 0111 | accept room invitation | Client
8 | 8 |1000 | refuse room invitation | Client
9 | 9 | 1001 | client has joined the room | Server
10 | A | 1010 | client has left the room | Server
11 | B | 1011 | send message | Client
12 | C | 1100 | broadcast message | Server
13 | D | 1101 | leave room | Client
14 | E | 1110 | disconnect | Client
15 | F | 1111 | ERROR | Client, Server
