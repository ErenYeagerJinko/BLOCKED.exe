package model;

import enums.*;
import exceptions.*;
import manager.*;
import util.*;
import java.util.*;

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
    private java.util.List<Integer> lastClearedRows = new ArrayList<>();
    private java.util.List<Integer> lastClearedCols = new ArrayList<>();
    private int lastLinePoints;
    private int totalLinesCleared;

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
        lastClearedRows.clear();
        lastClearedCols.clear();
        lastLinePoints = 0;
        totalLinesCleared = 0;
        Logger.getLogger().log("Game restarted");
        start();
    }

    // Used when resuming a saved session; values are clamped so a hand-edited file cannot break the timers
    public void restoreProgress(long elapsedMs, long remainingMs, int linesCleared) {
        this.elapsedTimeMs = Math.max(0, elapsedMs);
        if (gameMode == GameMode.TIME_ATTACK) {
            this.remainingTimeMs = Math.max(0, Math.min(remainingMs, TIME_ATTACK_DURATION_MS));
        }
        this.totalLinesCleared = Math.max(0, linesCleared);
    }

    public boolean tryPlacePiece(int slotIndex, int gridRow, int gridCol) throws InvalidPlacementException {
        tray.setSelectedSlotIndex(slotIndex);
        return tryPlaceSelectedPiece(gridRow, gridCol);
    }

    // An invalid placement throws before the tray is touched, so the piece is not consumed
    public boolean tryPlaceSelectedPiece(int gridRow, int gridCol) throws InvalidPlacementException {
        if (gameState != GameState.PLAYING) {
            return false;
        }
        int slot = tray.getSelectedSlotIndex();
        BlockPiece piece = tray.getSelectedPiece();
        if (piece == null) {
            return false;
        }

        board.placePiece(piece, gridRow, gridCol);
        if (scoreManager != null) {
            scoreManager.addPlacementScore(piece.getCells().size());
        }

        lastClearedRows = board.detectCompletedRows();
        lastClearedCols = board.detectCompletedColumns();
        lastLinePoints = 0;
        int lines = lastClearedRows.size() + lastClearedCols.size();
        if (lines > 0) {
            board.clearLines(lastClearedRows, lastClearedCols);
            totalLinesCleared += lines;
            if (scoreManager != null) {
                lastLinePoints = scoreManager.addLineClearScore(lines);
            }
        }

        tray.consumePiece(slot);
        checkGameOver();
        return true;
    }

    public void checkGameOver() {
        if (gameState != GameState.PLAYING) {
            return;
        }
        if (gameMode == GameMode.TIME_ATTACK && getCurrentScore() >= TIME_ATTACK_TARGET_SCORE) {
            Logger.getLogger().log("Time Attack target reached with " + remainingTimeMs + "ms left");
            endGame(GameState.VICTORY);
            return;
        }
        for (BlockPiece piece : tray.getRemainingPieces()) {
            if (board.canPieceFitAnywhere(piece)) {
                return;
            }
        }
        Logger.getLogger().log("No remaining tray piece fits on the board");
        endGame(GameState.GAME_OVER);
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

    public int getCurrentScore() {
        return scoreManager != null ? scoreManager.getCurrentScore() : 0;
    }

    public java.util.List<Integer> getLastClearedRows() {
        return new ArrayList<>(lastClearedRows);
    }

    public java.util.List<Integer> getLastClearedCols() {
        return new ArrayList<>(lastClearedCols);
    }

    public int getLastLinePoints() {
        return lastLinePoints;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }
}
