### Operation ID codes:

ID (dec) | ID (hex) | ID (bin) | Description | data field (String) | Sender | Command
--- | --- | --- | --- | --- | --- | ---
0 | 0 | 0000 | set sessionID | _none_ | Server | _none_
1 | 1 | 0001 | set name | `name` | Client | _none_
2 | 2 | 0010 | new client available | `client name` | Server | _none_
3 | 3 | 0011 | search for other client | _none_ | Client | `\search`
4 | 4 | 0100 | no other clients available | _none_ | Server | _none_
5 | 5 | 0101 | send room invitation | `client name` | Client | `\sendinv`
6 | 6 | 0110 | forward room invitation | `sending client name` | Server | _none_
7 | 7 | 0111 | accept room invitation | _none_ | Client | `\accept`
8 | 8 | 1000 | refuse room invitation | _none_ | Client | `\refuse`
9 | 9 | 1001 | client has joined the room | `client name` | Server | _none_
10 | A | 1010 | client has left the room | `client name` | Server | _none_
11 | B | 1011 | send message | `message` | Client | _none_
12 | C | 1100 | broadcast message | `name`: `message` | Server | _none_
13 | D | 1101 | leave room | _none_ | Client | `\leave`
14 | E | 1110 | disconnect | _none_ | Client | `\exit`
15 | F | 1111 | ERROR | `error code` | Client, Server | _none_


### Notes:
 - `name` is sending client's name
 - `client name` is **the other** client's name
