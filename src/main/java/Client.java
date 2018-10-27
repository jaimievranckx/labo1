import java.io.*;
import java.net.*;
import java.sql.ClientInfoStatus;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        boolean downloading = false;
        File file;
        FileOutputStream fop = null;
        byte[] input = new byte[8];
        String fromUser = "test";
        String fileName;
        ClientProtocol protocol = new ClientProtocol();
        while (input != null) {
            try (
                    Socket socket = new Socket(hostName, 9001);
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
            ) {
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    result.write(buffer, 0, length);
                }
                result.flush();
                input = result.toByteArray();
                if(protocol.state==protocol.DOWNLOAD){
                    System.out.println("DOWNLOADING...");
                }else {
                    System.out.println(new String(input));
                }
                byte[] bytes =protocol.processInput(input);
                    if(!(new String(bytes).equals("-1"))) {
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
                        socket.shutdownOutput();
                    }else{
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
                            socket.shutdownOutput();
                            socket.close();
                            input=null;
                    }
            }
        }
    }
}