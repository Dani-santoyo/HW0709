import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        // Define the port on which the server will listen
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                // Wait for a client to connect
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Create a new thread to handle the client
                new ServerThread(socket).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);

                // Respond with a message
                writer.println("Hello, Client!");

                // If the client sends "bye", terminate the connection
                if ("bye".equalsIgnoreCase(clientMessage)) {
                    break;
                }
            }

            socket.close();
            System.out.println("Client disconnected");

        } catch (IOException ex) {
            System.out.println("Server thread exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

