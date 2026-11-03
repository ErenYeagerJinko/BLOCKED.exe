package persistence;

import enums.*;
import exceptions.*;
import manager.*;
import model.*;
import util.*;

public class DataPersistenceManager {
    private final ScorePersistence scorePersistence;
    private final GameStateSerializer stateSerializer;

    public DataPersistenceManager() {
        this.scorePersistence = new ScorePersistence();
        this.stateSerializer = new GameStateSerializer();
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

    public void saveGameSession(Game game, ScoreManager scoreManager) {
        if (game == null || game.isOver()) {
            return;
        }
        int score = scoreManager != null ? scoreManager.getCurrentScore() : game.getCurrentScore();
        try {
            stateSerializer.saveGameState(game, score);
        } catch (PersistenceException e) {
            Logger.getLogger().error("Game session not saved: " + e.getMessage());
        }
    }

    public boolean resumeGameSession(Game game, ScoreManager scoreManager) {
        try {
            return stateSerializer.loadGameState(game, scoreManager);
        } catch (PersistenceException e) {
            Logger.getLogger().error("Saved session could not be resumed: " + e.getMessage());
            clearSavedSession();
            return false;
        }
    }

    public boolean hasSavedGame() {
        return getSavedGameMode() != null;
    }

    // Returns null when there is no save, and discards a save that cannot be read
    public GameMode getSavedGameMode() {
        if (!stateSerializer.hasSavedState()) {
            return null;
        }
        try {
            return stateSerializer.readSavedMode();
        } catch (PersistenceException e) {
            Logger.getLogger().error("Discarding unreadable saved session: " + e.getMessage());
            clearSavedSession();
            return null;
        }
    }

    public void clearSavedSession() {
        stateSerializer.deleteSavedState();
    }
}
