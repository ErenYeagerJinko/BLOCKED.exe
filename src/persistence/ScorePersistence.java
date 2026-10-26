package persistence;

import exceptions.*;
import java.io.*;

public class ScorePersistence {
    private static final String HIGH_SCORE_FILE = "data/best_score.txt";

    public ScorePersistence() {
    }

    public int loadHighScore() throws PersistenceException {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                return 0;
            }
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            throw new PersistenceException("Failed to read high score", e);
        }
    }

    public void saveHighScore(int score) throws PersistenceException {
        File file = new File(HIGH_SCORE_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(String.valueOf(score));
            writer.newLine();
        } catch (IOException e) {
            throw new PersistenceException("Failed to save high score", e);
        }
    }
}
