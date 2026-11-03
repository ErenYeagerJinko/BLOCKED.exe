package persistence;

import exceptions.*;
import util.*;

public class DataPersistenceManager {
    private final ScorePersistence scorePersistence;

    public DataPersistenceManager() {
        this.scorePersistence = new ScorePersistence();
    }

    // A corrupt or unreadable file should not stop the game, so fall back to 0
    public int loadBestScore() {
        try {
            return scorePersistence.loadHighScore();
        } catch (PersistenceException e) {
            Logger.getLogger().error("Best score unavailable, using 0: " + e.getMessage());
            return 0;
        }
    }

    public void saveBestScore(int score) {
        try {
            scorePersistence.saveHighScore(score);
        } catch (PersistenceException e) {
            Logger.getLogger().error("Best score not saved: " + e.getMessage());
        }
    }
}
