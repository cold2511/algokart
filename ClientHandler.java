import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean loggedIn = false;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                line = line.trim();
                if (line.isEmpty()) continue;

                // Handle login
                if (!loggedIn) {
                    if (line.startsWith("LOGIN ")) {
                        String name = line.substring(6).trim();
                        if (name.isEmpty()) {
                            writer.println("ERR invalid-username");
                            continue;
                        }

                        if (!ChatServer.addClient(name, this)) {
                            writer.println("ERR username-taken");
                            socket.close();
                            return;
                        }

                        this.username = name;
                        this.loggedIn = true;
                        writer.println("OK");
                        System.out.println(username + " logged in");
                    } else {
                        writer.println("ERR please-login-first");
                    }
                    continue;
                }

                // Handle messages
                if (line.startsWith("MSG ")) {
                    String text = line.substring(4).trim();
                    if (!text.isEmpty()) {
                        ChatServer.broadcast("MSG " + username + " " + text, username);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        } finally {
            if (loggedIn) {
                ChatServer.removeClient(username);
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
