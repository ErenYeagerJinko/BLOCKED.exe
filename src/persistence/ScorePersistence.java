package persistence;

import exceptions.*;
import util.*;
import java.io.*;

public class ScorePersistence {
    private static final String HIGH_SCORE_FILE = "data/best_score.txt";

    public ScorePersistence() {
    }

    public int loadHighScore() throws PersistenceException {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            Logger.getLogger().log("High score file not found, defaulting to 0");
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                return 0;
            }
            int score = Integer.parseInt(line.trim());
            Logger.getLogger().log("Loaded high score: " + score);
            return score;
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger().error("Failed to read high score: " + e.getMessage());
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
            Logger.getLogger().log("Saved high score: " + score);
        } catch (IOException e) {
            Logger.getLogger().error("Failed to save high score: " + e.getMessage());
            throw new PersistenceException("Failed to save high score", e);
        }
    }
}
