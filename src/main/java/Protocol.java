import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

public class Protocol {


    private static final int AWAITDECISSION = 1;
    private static final int STARTDOWNLOAD = 3;
    private static final int DOWNLOAD = 4;
    private static final int ENDCONNECTION = 5;

    private int state = AWAITDECISSION;
    private int fileNumber = 0;
    public byte[] processInput(String theInput) throws UnsupportedEncodingException {
        byte[] theOutput = null;
        switch (state) {
            case STARTDOWNLOAD: fileNumber = Integer.parseInt(theInput.toString());
                theOutput = "DOWNLOAD".getBytes();
                    File dir = new File("C:/Users/Jaimie/Documents/GitHub/labo1/src/main/database");
                    File[] directoryListing = dir.listFiles();
                    if (directoryListing != null) {
                        try {
                            theOutput = Files.readAllBytes(directoryListing[fileNumber-1].toPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    state = DOWNLOAD;
                break;
            case DOWNLOAD: int code = Integer.parseInt(theInput.toString());
                switch (code){
                    case 0: theOutput = "DOWNLOAD SUCCESSFUL".getBytes();
                        state = ENDCONNECTION;
                        break;
                    case 1: theOutput = "FAILED".getBytes();
                        state = ENDCONNECTION;
                        break;
                }
                break;
            case ENDCONNECTION:
                if(theInput.equalsIgnoreCase("y")){
                    theOutput = "-1".getBytes();
                }else if(theInput.equalsIgnoreCase("n")){
                    state = AWAITDECISSION;
                }
            case AWAITDECISSION:
                state = STARTDOWNLOAD;
                String temp = "Choose file to download\n";
                dir = new File("C:/Users/Jaimie/Documents/GitHub/labo1/src/main/database");
                directoryListing = dir.listFiles();
                int numberOfFile = 1;
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        temp += numberOfFile + "." + child.getName() + "\n";
                        numberOfFile++;
                    }
                }
                theOutput = temp.getBytes();
                break;
        }
        return theOutput;
    }
}