package FileLoader;

import java.util.Scanner;

public class ReadFile {
    private final String convertedContents;

    public ReadFile(String targetFile) {
        this.convertedContents = parseFileToString(targetFile);
    }

    public String parseFileToString(String targetFilePath){
        StringBuilder sb = new StringBuilder();

        try {
            Scanner myReader = new Scanner(targetFilePath);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
//                System.out.println(data);
                sb.append(data);
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getConvertedContents(){
        return this.convertedContents;
    }
}
