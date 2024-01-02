import java.io.*;
import java.net.*;

public class p2psharing {

    public static void main(String[] args) {
        try {
            // Create a server socket for listening to incoming connections
            ServerSocket serverSocket = new ServerSocket(9000);
            System.out.println("Server socket created");

            // Loop to accept incoming connections from peers
            while (true) {
                // Accept a connection from a peer
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread to handle the connection
                PeerHandler handler = new PeerHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error creating server socket: " + e.getMessage());
        }
    }

    // Inner class for handling connections from peers
    private static class PeerHandler implements Runnable {
        private Socket clientSocket;

        public PeerHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                // Create input and output streams for the connection
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                // Read the file name from the input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String fileName = reader.readLine();
                System.out.println("File requested: " + fileName);

                // Open the file and send its contents to the output stream
                File file = new File(fileName);
                FileInputStream fileInput = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = fileInput.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                System.out.println("File sent successfully");

                // Close the streams and the socket
                fileInput.close();
                output.close();
                input.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error handling peer connection: " + e.getMessage());
            }
        }
    }
}
