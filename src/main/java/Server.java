import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 9001;
        while(true) {
            try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
            ) {
                System.out.println("Waiting for client");
                // Initiate conversation with client
                Protocol protocol = new Protocol();
                String input = "test";
                while (input != null) {
                    Socket clientSocket = serverSocket.accept();
                    OutputStream out = clientSocket.getOutputStream();
                    InputStream in = clientSocket.getInputStream();
                    byte[] bytes = protocol.processInput(input);

                    if (!(new String(bytes).equals("-1"))) {
                        int position = 0;
                        while (position < bytes.length) {
                            if (bytes.length - position > 4096) {
                                out.write(bytes, position, 4096);
                                position += 4096;
                            } else {
                                out.write(bytes, position, bytes.length - position);
                                position += bytes.length - position;
                            }
                        }
                        clientSocket.shutdownOutput();
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            result.write(buffer, 0, length);
                        }
                        input = result.toString("UTF-8");
                    } else {
                        System.out.println("disconnecting currentClient");
                        clientSocket.close();
                        input = null;
                    }
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
}