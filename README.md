# algokart

A simple multi-client TCP chat server implemented using **Java sockets** only (no frameworks).

## 🚀 Features
- Handles multiple clients simultaneously
- Login using a unique username
- Real-time broadcast messages
- Disconnect notifications
- 

## 🧠 Commands
| Command | Description |
|----------|--------------|
| `LOGIN <username>` | Logs in the user |
| `MSG <text>` | Broadcasts message to all users |

## ⚙️ How to Run
```bash
javac ChatServer.java ClientHandler.java
java ChatServer

## (install and enable telnet on your windows first)
## after that open two cmd windows and type "telnet localhost 4000"  on both of them->you should see client connected on the server terminal

## after that u can type the commands and see the typed message on both the client window



VIDEO LINK:https://www.loom.com/share/73c91a67f2844f4ebab1e70eae84a776
