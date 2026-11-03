package persistence;

import enums.*;
import exceptions.*;
import manager.*;
import model.*;
import util.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/*
 * File layout (one key=value per line):
 *   mode, score, elapsed, remaining, lines
 *   tray=SHAPE,SHAPE,-        ('-' marks an empty slot)
 *   row=RRGGBB,.,...          (8 lines, '.' marks an empty cell)
 */
public class GameStateSerializer {
    private static final String SAVE_STATE_FILE = "data/saved_game.txt";
    private static final String EMPTY_CELL = ".";
    private static final String EMPTY_SLOT = "-";

    public GameStateSerializer() {
    }

    public void saveGameState(Game game, int currentScore) throws PersistenceException {
        if (game == null) {
            throw new PersistenceException("No game to save");
        }
        File file = new File(SAVE_STATE_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writeLine(writer, "mode", game.getGameMode().name());
            writeLine(writer, "score", String.valueOf(currentScore));
            writeLine(writer, "elapsed", String.valueOf(game.getElapsedTimeMs()));
            writeLine(writer, "remaining", String.valueOf(game.getRemainingTimeMs()));
            writeLine(writer, "lines", String.valueOf(game.getTotalLinesCleared()));
            writeLine(writer, "tray", encodeTray(game.getTray()));
            for (int r = 0; r < Board.GRID_SIZE; r++) {
                writeLine(writer, "row", encodeRow(game.getBoard(), r));
            }
            Logger.getLogger().log("Game session saved (" + game.getGameMode() + ", score=" + currentScore + ")");
        } catch (IOException e) {
            Logger.getLogger().error("Failed to save game session: " + e.getMessage());
            throw new PersistenceException("Failed to save game session", e);
        }
    }

    public boolean loadGameState(Game game, ScoreManager scoreManager) throws PersistenceException {
        if (game == null || !hasSavedState()) {
            return false;
        }
        SavedState state = readState();
        if (state.mode != game.getGameMode()) {
            throw new PersistenceException("Saved game is " + state.mode + " but game is " + game.getGameMode());
        }

        Board board = game.getBoard();
        board.reset();
        for (int r = 0; r < Board.GRID_SIZE; r++) {
            for (int c = 0; c < Board.GRID_SIZE; c++) {
                if (state.cells[r][c] != null) {
                    board.setCellOccupied(r, c, state.cells[r][c]);
                }
            }
        }
        game.getTray().setSlots(state.slots);
        game.restoreProgress(state.elapsedMs, state.remainingMs, state.linesCleared);
        if (scoreManager != null) {
            scoreManager.setCurrentScore(state.score);
        }
        Logger.getLogger().log("Game session restored (" + state.mode + ", score=" + state.score + ")");
        return true;
    }

    public GameMode readSavedMode() throws PersistenceException {
        return readState().mode;
    }

    public boolean hasSavedState() {
        return new File(SAVE_STATE_FILE).exists();
    }

    public void deleteSavedState() {
        File file = new File(SAVE_STATE_FILE);
        if (file.exists() && !file.delete()) {
            Logger.getLogger().error("Could not delete saved game file: " + SAVE_STATE_FILE);
        }
    }

    // Parses and validates the whole file before anything touches the live game
    private SavedState readState() throws PersistenceException {
        Map<String, String> values = new HashMap<>();
        java.util.List<String> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_STATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int sep = line.indexOf('=');
                if (sep <= 0) {
                    continue;
                }
                String key = line.substring(0, sep).trim();
                String value = line.substring(sep + 1).trim();
                if ("row".equals(key)) {
                    rows.add(value);
                } else {
                    values.put(key, value);
                }
            }
        } catch (IOException e) {
            Logger.getLogger().error("Failed to read saved game: " + e.getMessage());
            throw new PersistenceException("Failed to read saved game", e);
        }

        try {
            SavedState state = new SavedState();
            state.mode = GameMode.valueOf(require(values, "mode"));
            state.score = Integer.parseInt(require(values, "score"));
            state.elapsedMs = Long.parseLong(require(values, "elapsed"));
            state.remainingMs = Long.parseLong(require(values, "remaining"));
            state.linesCleared = Integer.parseInt(require(values, "lines"));
            state.slots = decodeTray(require(values, "tray"));
            state.cells = decodeBoard(rows);
            return state;
        } catch (IllegalArgumentException | InvalidPieceException e) {
            Logger.getLogger().error("Saved game file is corrupt: " + e.getMessage());
            throw new PersistenceException("Saved game file is corrupt", e);
        }
    }

    private static String require(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing '" + key + "'");
        }
        return value;
    }

    private static void writeLine(BufferedWriter writer, String key, String value) throws IOException {
        writer.write(key + "=" + value);
        writer.newLine();
    }

    private static String encodeTray(Tray tray) {
        StringJoiner joiner = new StringJoiner(",");
        for (BlockPiece piece : tray.getSlots()) {
            joiner.add(piece != null ? piece.getShapeType().name() : EMPTY_SLOT);
        }
        return joiner.toString();
    }

    private static BlockPiece[] decodeTray(String value) throws InvalidPieceException {
        String[] parts = value.split(",");
        if (parts.length != Tray.SLOT_COUNT) {
            throw new IllegalArgumentException("Expected " + Tray.SLOT_COUNT + " tray slots");
        }
        BlockPiece[] slots = new BlockPiece[Tray.SLOT_COUNT];
        for (int i = 0; i < parts.length; i++) {
            String name = parts[i].trim();
            slots[i] = EMPTY_SLOT.equals(name) ? null : PieceGenerator.createPiece(ShapeType.valueOf(name));
        }
        return slots;
    }

    private static String encodeRow(Board board, int row) {
        StringJoiner joiner = new StringJoiner(",");
        for (int c = 0; c < Board.GRID_SIZE; c++) {
            Cell cell = board.getCell(row, c);
            if (cell != null && cell.isOccupied() && cell.getColor() != null) {
                joiner.add(String.format("%06X", cell.getColor().getRGB() & 0xFFFFFF));
            } else {
                joiner.add(EMPTY_CELL);
            }
        }
        return joiner.toString();
    }

    private static Color[][] decodeBoard(java.util.List<String> rows) {
        if (rows.size() != Board.GRID_SIZE) {
            throw new IllegalArgumentException("Expected " + Board.GRID_SIZE + " board rows");
        }
        Color[][] cells = new Color[Board.GRID_SIZE][Board.GRID_SIZE];
        for (int r = 0; r < Board.GRID_SIZE; r++) {
            String[] parts = rows.get(r).split(",");
            if (parts.length != Board.GRID_SIZE) {
                throw new IllegalArgumentException("Row " + r + " has " + parts.length + " cells");
            }
            for (int c = 0; c < Board.GRID_SIZE; c++) {
                String token = parts[c].trim();
                cells[r][c] = EMPTY_CELL.equals(token) ? null : new Color(Integer.parseInt(token, 16));
            }
        }
        return cells;
    }

    private static class SavedState {
        GameMode mode;
        int score;
        long elapsedMs;
        long remainingMs;
        int linesCleared;
        BlockPiece[] slots;
        Color[][] cells;
    }
}
