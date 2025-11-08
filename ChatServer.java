import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int DEFAULT_PORT = 4000;
    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using default 4000");
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // Broadcasts a message to all clients
    public static void broadcast(String message, String excludeUser) {
        for (ClientHandler client : clients.values()) {
            if (!client.getUsername().equals(excludeUser)) {
                client.sendMessage(message);
            }
        }
    }

    // Add client
    public static boolean addClient(String username, ClientHandler handler) {
        if (clients.containsKey(username)) {
            return false;
        }
        clients.put(username, handler);
        return true;
    }

    // Remove client
    public static void removeClient(String username) {
        clients.remove(username);
        broadcast("INFO " + username + " disconnected", username);
    }
}
