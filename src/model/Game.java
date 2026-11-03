package model;

import enums.*;
import manager.*;
import util.*;

public class Game {
    public static final long TIME_ATTACK_DURATION_MS = 180000;
    public static final int TIME_ATTACK_TARGET_SCORE = 500;

    private final GameMode gameMode;
    private final ScoreManager scoreManager;
    private Board board;
    private Tray tray;
    private GameState gameState;
    private long elapsedTimeMs;
    private long remainingTimeMs;

    public Game(GameMode mode, ScoreManager scoreManager) {
        this.gameMode = mode != null ? mode : GameMode.CLASSIC;
        this.scoreManager = scoreManager;
        this.board = new Board();
        this.tray = new Tray();
        this.gameState = GameState.MENU;
        resetTimers();
    }

    public void start() {
        gameState = GameState.PLAYING;
        Logger.getLogger().log("Game started in " + gameMode + " mode");
    }

    public void pause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            Logger.getLogger().log("Game paused at " + elapsedTimeMs + "ms");
        }
    }

    public void resume() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
            Logger.getLogger().log("Game resumed at " + elapsedTimeMs + "ms");
        }
    }

    public void togglePause() {
        if (gameState == GameState.PLAYING) {
            pause();
        } else if (gameState == GameState.PAUSED) {
            resume();
        }
    }

    public void restart() {
        board.reset();
        tray = new Tray();
        if (scoreManager != null) {
            scoreManager.resetCurrentScore();
        }
        resetTimers();
        Logger.getLogger().log("Game restarted");
        start();
    }

    // Time only advances while playing, so a paused game resumes exactly where it stopped
    public void updateTime(long deltaMs) {
        if (gameState != GameState.PLAYING || deltaMs <= 0) {
            return;
        }
        elapsedTimeMs += deltaMs;
        if (gameMode == GameMode.TIME_ATTACK) {
            remainingTimeMs = Math.max(0, remainingTimeMs - deltaMs);
            if (remainingTimeMs == 0) {
                Logger.getLogger().log("Time Attack expired before reaching the target score");
                endGame(GameState.GAME_OVER);
            }
        }
    }

    private void endGame(GameState finalState) {
        gameState = finalState;
        if (scoreManager != null) {
            scoreManager.updateBestScore();
        }
        Logger.getLogger().log("Game ended with state " + finalState);
    }

    private void resetTimers() {
        elapsedTimeMs = 0;
        remainingTimeMs = gameMode == GameMode.TIME_ATTACK ? TIME_ATTACK_DURATION_MS : 0;
    }

    public boolean isPlaying() {
        return gameState == GameState.PLAYING;
    }

    public boolean isOver() {
        return gameState == GameState.GAME_OVER || gameState == GameState.VICTORY;
    }

    public Board getBoard() {
        return board;
    }

    public Tray getTray() {
        return tray;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public long getElapsedTimeMs() {
        return elapsedTimeMs;
    }

    public long getRemainingTimeMs() {
        return remainingTimeMs;
    }
}
