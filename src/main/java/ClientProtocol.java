import java.io.*;
import java.nio.file.Files;

public class ClientProtocol {


    private static final int CHOOSEFILE = 1;
    public static final int DOWNLOAD = 2;
    private static final int ENDCONNECTION = 3;
    private static final int SHUTDOWN = 4;

    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public int state = CHOOSEFILE;
    private String fileName = "";
    String userInput;
    FileOutputStream fop;
    public byte[] processInput(byte[] theInput) throws IOException {
        byte[] theOutput = null;
        switch (state) {
            case CHOOSEFILE:
                userInput = stdIn.readLine();
                int fileNumber = Integer.parseInt(userInput);
                BufferedReader bufReader = new BufferedReader(new StringReader(new String(theInput)));
                String line = null;
                fileName = "";
                for (int current = 0; current <= fileNumber; current++) {
                    line = bufReader.readLine();
                }
                boolean reading = false;
                for (char ch : line.toCharArray()) {
                    if (reading) {
                        fileName += ch;
                    }
                    if (ch == '.') {
                        reading = true;
                    }
                }
                bufReader.close();
                theOutput = userInput.getBytes();
                state = DOWNLOAD;
                break;
            case DOWNLOAD:
                File file = new File("C:/Users/Jaimie/Documents/GitHub/labo1/src/main/clientDirectory/" + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fop= new FileOutputStream(file);
                System.out.println("file size = "+theInput.length+" bytes");
                fop.write(theInput);
                fop.flush();
                fop.close();
                theOutput = "0".getBytes();
                state = ENDCONNECTION;
                System.out.println("DOWNLOAD COMPLETE");
                break;
            case ENDCONNECTION:
                System.out.println("End the connection? y/n");
                userInput = stdIn.readLine();
                theOutput = userInput.getBytes();
                if(userInput.equals("y")){
                    theOutput = "y".getBytes();
                    state = SHUTDOWN;
                    System.out.print("exiting connection");
                }else{
                    theOutput = "n".getBytes();
                    state = CHOOSEFILE;
                }
                break;
            case SHUTDOWN:
                System.out.print("End the connection? y/n");
                    theOutput = "-1".getBytes();
                    System.out.print("exiting application");

                break;
        }
        return theOutput;
    }
}
