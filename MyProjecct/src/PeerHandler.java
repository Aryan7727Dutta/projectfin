import java.io.*;
import java.net.Socket;

public class PeerHandler implements Runnable {
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
