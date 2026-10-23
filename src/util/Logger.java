package util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final static Logger logger = new Logger();
    private BufferedWriter writer;

    private Logger() {
        try {
            new File("logs").mkdirs();
            writer = new BufferedWriter(new FileWriter("logs/app.log", true));
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public void log(String message) {
        writeToFile("[INFO]", message);
    }

    public void error(String message) {
        writeToFile("[ERROR]", message);
    }

    private void writeToFile(String level, String message) {
        if (writer == null)
            return;
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(formattedTimestamp + " " + level + " " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write to log: " + e.getMessage());
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Failed to close logger: " + e.getMessage());
            }
        }
    }
}
