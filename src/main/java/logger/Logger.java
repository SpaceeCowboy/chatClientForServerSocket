package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static final String PATH = "C:\\Users\\lshap\\IdeaProjects\\client\\src\\main\\java\\";

    public void log(String msg) {
        try (FileWriter writer = new FileWriter(PATH + "clientLogger.txt", true)) {
            writer.write(msg);
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

}






