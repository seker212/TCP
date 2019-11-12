### Operation ID codes:

ID (dec) | ID (hex) | ID (bin) | Description | data field (String) | Sender | Command | Server Ans.
--- | --- | --- | --- | --- | --- | --- | ---
0 | 0 | 0000 | Empty Header | _none_ | _none_ | _none_ | _none_
1 | 1 | 0001 | get sessionID | _none_ | Client | _none_ | 1 - Header with new session ID
2 | 2 | 0010 | set name | `name` | Client | _none_ | 1 - 'Set'; 2 - 'Taken'
3 | 3 | 0011 | send room invitation | _none_ | Client | `\sendinv` | 1 - 'accepted', 2 - 'refused', 3 - 'out of reach'
4 | 4 | 0100 | accept room invitation | _none_ | Client | `\accept` |
5 | 5 | 0101 | refuse room invitation | _none_ | Client | `\refuse` |
6 | 6 | 0110 | send message | `message` | Client | _none_
7 | 7 | 0111 | leave room | _none_ | Client | `\leave`
8 | 8 | 1000 | disconnect | _none_ | Client | `\exit`
9 | 9 | 1001 | print message | `name`: `message`/`server message` | Server | _none_
10 | 10 | 1010 | ERROR | `error code` | Client, Server | _none_



### Notes:
 - `name` is sending client's name
 - `client name` is **the other** client's name
