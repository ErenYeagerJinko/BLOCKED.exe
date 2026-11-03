package manager;

import persistence.*;
import util.*;

public class ScoreManager {
    public static final int POINTS_PER_BLOCK = 1;
    public static final int POINTS_PER_LINE = 100;

    private final DataPersistenceManager persistenceManager;
    private int currentScore;
    private int bestScore;

    public ScoreManager(DataPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
        this.currentScore = 0;
        this.bestScore = persistenceManager != null ? persistenceManager.loadBestScore() : 0;
    }

    public void addPlacementScore(int blockCount) {
        if (blockCount <= 0) {
            return;
        }
        currentScore += blockCount * POINTS_PER_BLOCK;
    }

    // Clearing several lines in one move multiplies the reward: 1 line = 100, 2 = 400, 3 = 900
    public int addLineClearScore(int linesCleared) {
        if (linesCleared <= 0) {
            return 0;
        }
        int points = linesCleared * POINTS_PER_LINE * linesCleared;
        currentScore += points;
        Logger.getLogger().log("Cleared " + linesCleared + " line(s) for " + points + " points");
        return points;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void resetCurrentScore() {
        currentScore = 0;
    }

    public void updateBestScore() {
        if (currentScore <= bestScore) {
            return;
        }
        bestScore = currentScore;
        if (persistenceManager != null) {
            persistenceManager.saveBestScore(bestScore);
        }
        Logger.getLogger().log("New best score: " + bestScore);
    }
}
