package controller;

import audio.*;
import model.*;
import util.*;
import view.*;
import java.awt.event.*;

public class KeyboardInputHandler extends KeyAdapter {
    private GamePanel panel;
    private Game game;
    private SoundManager soundManager;
    private int cursorRow;
    private int cursorCol;

    public KeyboardInputHandler(GamePanel panel, Game game, SoundManager soundManager) {
        this.panel = panel;
        this.game = game;
        this.soundManager = soundManager;
        resetCursor();
        Logger.getLogger().log("KeyboardInputHandler initialized");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (game == null || panel == null) {
            return;
        }
        if (handleHotkey(e.getKeyCode())) {
            panel.repaint();
            return;
        }
        if (!game.isPlaying()) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
            case KeyEvent.VK_NUMPAD1:
                selectSlot(0);
                break;
            case KeyEvent.VK_2:
            case KeyEvent.VK_NUMPAD2:
                selectSlot(1);
                break;
            case KeyEvent.VK_3:
            case KeyEvent.VK_NUMPAD3:
                selectSlot(2);
                break;
            case KeyEvent.VK_UP:
                moveCursor(-1, 0);
                break;
            case KeyEvent.VK_DOWN:
                moveCursor(1, 0);
                break;
            case KeyEvent.VK_LEFT:
                moveCursor(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                moveCursor(0, 1);
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                placeSelectedPiece();
                break;
            default:
                return;
        }
        panel.repaint();
    }

    // Pause, restart and mute stay available while paused, unlike piece controls
    private boolean handleHotkey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_P:
                if (!game.isOver()) {
                    panel.togglePause();
                }
                return true;
            case KeyEvent.VK_R:
                Logger.getLogger().log("Restart requested from keyboard");
                panel.restartGame();
                return true;
            case KeyEvent.VK_M:
                if (soundManager != null) {
                    soundManager.toggleMute();
                }
                return true;
            default:
                return false;
        }
    }

    private void selectSlot(int slotIndex) {
        if (!game.getTray().hasPiece(slotIndex)) {
            return;
        }
        panel.selectSlot(slotIndex);
        clampCursor();
        Logger.getLogger().log("Keyboard selected tray slot " + slotIndex);
    }

    private void moveCursor(int dRow, int dCol) {
        if (game.getTray().getSelectedPiece() == null) {
            return;
        }
        cursorRow += dRow;
        cursorCol += dCol;
        clampCursor();
    }

    private void placeSelectedPiece() {
        int slot = game.getTray().getSelectedSlotIndex();
        if (slot == Tray.NO_SELECTION) {
            return;
        }
        Logger.getLogger().log("Keyboard placement of slot " + slot + " at (" + cursorRow + ", " + cursorCol + ")");
        panel.handlePiecePlacement(slot, cursorRow, cursorCol);
    }

    // Keeps the whole selected piece on the board so the preview never hangs off an edge
    private void clampCursor() {
        BlockPiece piece = game.getTray().getSelectedPiece();
        int maxRow = Board.GRID_SIZE - (piece != null ? GamePanel.getPieceRows(piece) : 1);
        int maxCol = Board.GRID_SIZE - (piece != null ? GamePanel.getPieceCols(piece) : 1);
        cursorRow = Math.max(0, Math.min(cursorRow, maxRow));
        cursorCol = Math.max(0, Math.min(cursorCol, maxCol));
    }

    public void resetCursor() {
        cursorRow = Board.GRID_SIZE / 2 - 1;
        cursorCol = Board.GRID_SIZE / 2 - 1;
    }

    public int getCursorRow() {
        return cursorRow;
    }

    public int getCursorCol() {
        return cursorCol;
    }

    public void setGame(Game game) {
        this.game = game;
        resetCursor();
    }

    public Game getGame() {
        return game;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}
