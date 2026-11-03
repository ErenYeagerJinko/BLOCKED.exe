package view;

import animation.*;
import audio.*;
import controller.*;
import enums.*;
import exceptions.*;
import manager.*;
import model.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class GamePanel extends JPanel {
    public static final int FPS = 60;

    // Tray geometry is shared with MouseInputHandler's slot hit-testing
    public static final int TRAY_AREA_HEIGHT = 140;
    public static final int TRAY_BOTTOM_MARGIN = 20;
    public static final int TRAY_SLOT_WIDTH = 110;
    public static final int TRAY_SLOT_GAP = 20;

    private static final int HUD_HEIGHT = 90;
    private static final int BOARD_MARGIN = 20;
    private static final String SLOT_IMAGE = "resources/UI/slot/background.png";
    private static final String STAR_IMAGE = "resources/UI/best/star.png";
    private static final String PAUSE_BUTTON_IMAGE = "resources/UI/pause/button.png";

    private static final Color BOARD_BACKGROUND = new Color(10, 14, 40, 210);
    private static final Color EMPTY_CELL = new Color(255, 255, 255, 25);
    private static final Color HUD_LABEL = new Color(170, 180, 220);
    private static final Color SELECTED_SLOT = new Color(255, 215, 0);
    private static final Color INVALID_PREVIEW = new Color(255, 70, 70, 150);
    private static final long LINE_CLEAR_FLASH_MS = 400;

    private final GameFrame frame;
    private final AnimationManager animationManager;
    private final MouseInputHandler mouseHandler;
    private final KeyboardInputHandler keyboardHandler;
    private final PauseOverlay pauseOverlay;
    private final JButton pauseButton;
    private final Timer renderTimer;
    private Game game;
    private long lastTickNanos;
    private boolean endScreenShown;

    public GamePanel(GameFrame frame) {
        this.frame = frame;
        this.animationManager = new AnimationManager();
        this.renderTimer = new Timer(1000 / FPS, e -> tick());
        this.mouseHandler = new MouseInputHandler(this, null);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        this.keyboardHandler = new KeyboardInputHandler(this, null, SoundManager.getInstance());
        addKeyListener(keyboardHandler);
        setOpaque(true);
        setBackground(new Color(18, 18, 32));
        setFocusable(true);

        setLayout(null);
        pauseOverlay = new PauseOverlay(this);
        pauseOverlay.setVisible(false);
        pauseButton = UIButtonRenderer.createButton("Pause", PAUSE_BUTTON_IMAGE, 100, 30);
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> togglePause());
        add(pauseOverlay);
        add(pauseButton);
    }

    @Override
    public void doLayout() {
        pauseOverlay.setBounds(0, 0, getWidth(), getHeight());
        pauseButton.setBounds(getWidth() - 130, 68, 100, 26);
    }

    public void togglePause() {
        if (game == null) {
            return;
        }
        if (game.isPlaying()) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    public void pauseGame() {
        if (game == null || !game.isPlaying()) {
            return;
        }
        game.pause();
        mouseHandler.resetDrag();
        pauseOverlay.setVisible(true);
        repaint();
    }

    public void resumeGame() {
        if (game == null || game.getGameState() != GameState.PAUSED) {
            return;
        }
        game.resume();
        // Restart the frame clock so no paused time leaks into the first tick
        lastTickNanos = System.nanoTime();
        pauseOverlay.setVisible(false);
        requestFocusInWindow();
        repaint();
    }

    public void restartGame() {
        if (frame != null) {
            frame.restartGame();
        }
    }

    public void goToMenu() {
        pauseOverlay.setVisible(false);
        if (frame != null) {
            frame.returnToMenu();
        }
    }

    public void setGame(Game game) {
        this.game = game;
        mouseHandler.setGame(game);
        mouseHandler.resetDrag();
        keyboardHandler.setGame(game);
        endScreenShown = false;
        pauseOverlay.setVisible(false);
        animationManager.clear();
        lastTickNanos = System.nanoTime();
        if (!renderTimer.isRunning()) {
            renderTimer.start();
        }
    }

    public void stopRendering() {
        renderTimer.stop();
    }

    // All time-based updates happen here on the timer; paintComponent only reads state
    private void tick() {
        long now = System.nanoTime();
        long deltaMs = (now - lastTickNanos) / 1_000_000;
        lastTickNanos += deltaMs * 1_000_000;

        if (game != null) {
            game.updateTime(deltaMs);
        }
        animationManager.update(deltaMs);
        if (game != null && game.isOver() && !animationManager.hasActiveAnimations()) {
            showEndScreen();
        }
        repaint();
    }

    // Waits for running animations to finish so the final move stays visible
    private void showEndScreen() {
        if (endScreenShown) {
            return;
        }
        endScreenShown = true;
        stopRendering();
        mouseHandler.resetDrag();
        pauseOverlay.setVisible(false);

        boolean victory = game.getGameState() == GameState.VICTORY;
        if (!victory) {
            SoundManager.getInstance().playGameOver();
        }
        ScoreManager scoreManager = game.getScoreManager();
        int best = scoreManager != null ? scoreManager.getBestScore() : game.getCurrentScore();
        if (frame != null) {
            frame.recordFinishedGame(game);
            frame.showGameOver(game.getCurrentScore(), best, victory);
        }
    }

    public void selectSlot(int slotIndex) {
        if (game == null || !game.isPlaying() || !game.getTray().hasPiece(slotIndex)) {
            return;
        }
        game.getTray().setSelectedSlotIndex(slotIndex);
        SoundManager.getInstance().playSelect();
        requestFocusInWindow();
        repaint();
    }

    // Drops outside the board cancel the drag instead of counting as an invalid move
    public void handleDrop(int slotIndex, Point dropPoint) {
        BlockPiece piece = game != null ? game.getTray().getPiece(slotIndex) : null;
        if (piece == null || dropPoint == null || !isOverBoard(dropPoint)) {
            return;
        }
        int[] cell = getDropCell(piece, dropPoint);
        handlePiecePlacement(slotIndex, cell[0], cell[1]);
    }

    public void handlePiecePlacement(int slotIndex, int gridRow, int gridCol) {
        if (game == null || !game.isPlaying()) {
            return;
        }
        BlockPiece piece = game.getTray().getPiece(slotIndex);
        if (piece == null) {
            return;
        }

        try {
            if (!game.tryPlacePiece(slotIndex, gridRow, gridCol)) {
                return;
            }
        } catch (InvalidPlacementException e) {
            SoundManager.getInstance().playInvalid();
            return;
        }

        int cellSize = getCellSize();
        int originX = getBoardOriginX();
        int originY = getBoardOriginY();
        animationManager.addAnimation(new PlacementPopAnimation(piece, gridRow, gridCol, originX, originY, cellSize));
        SoundManager.getInstance().playPlace();

        int linePoints = game.getLastLinePoints();
        if (linePoints > 0) {
            int boardCenter = cellSize * Board.GRID_SIZE / 2;
            animationManager.addAnimation(new RowClearAnimation(game.getLastClearedRows(), game.getLastClearedCols(),
                    originX, originY, cellSize, LINE_CLEAR_FLASH_MS));
            animationManager.addAnimation(new ScorePopupAnimation(linePoints, originX + boardCenter, originY + boardCenter));
            SoundManager.getInstance().playClear();
        }
        repaint();
    }

    // Top-left board cell for a piece held by its centre, returned as {row, col}
    public int[] getDropCell(BlockPiece piece, Point p) {
        int cellSize = getCellSize();
        double col = (p.x - getBoardOriginX()) / (double) cellSize - getPieceCols(piece) / 2.0;
        double row = (p.y - getBoardOriginY()) / (double) cellSize - getPieceRows(piece) / 2.0;
        return new int[] {(int) Math.round(row), (int) Math.round(col)};
    }

    private boolean isOverBoard(Point p) {
        int boardPx = getCellSize() * Board.GRID_SIZE;
        return p.x >= getBoardOriginX() && p.x < getBoardOriginX() + boardPx
                && p.y >= getBoardOriginY() && p.y < getBoardOriginY() + boardPx;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BackgroundRenderer.renderGameBackground(g, this);
        if (game == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawHUD(g2d);
        drawBoard(g2d);
        drawTray(g2d);
        animationManager.renderAll(g2d);
        if (mouseHandler.isDragging()) {
            drawDragPreview(g2d);
        } else {
            drawKeyboardPreview(g2d);
        }
        g2d.dispose();
    }

    private void drawKeyboardPreview(Graphics2D g2d) {
        BlockPiece piece = game.getTray().getSelectedPiece();
        if (piece == null || !game.isPlaying()) {
            return;
        }
        drawPlacementGhost(g2d, piece, keyboardHandler.getCursorRow(), keyboardHandler.getCursorCol());
    }

    private void drawDragPreview(Graphics2D g2d) {
        Point p = mouseHandler.getCurrentDragPoint();
        BlockPiece piece = game.getTray().getPiece(mouseHandler.getDraggedSlot());
        if (piece == null || p == null || !game.isPlaying()) {
            return;
        }

        if (isOverBoard(p)) {
            int[] cell = getDropCell(piece, p);
            drawPlacementGhost(g2d, piece, cell[0], cell[1]);
            return;
        }

        int size = getCellSize();
        int startX = p.x - getPieceCols(piece) * size / 2;
        int startY = p.y - getPieceRows(piece) * size / 2;
        Composite original = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        for (Cell cell : piece.getCells()) {
            drawBlock(g2d, startX + cell.getCol() * size, startY + cell.getRow() * size, size, piece.getColor());
        }
        g2d.setComposite(original);
    }

    // Shows the snapped piece faded where it fits, or red cells where it would collide
    private void drawPlacementGhost(Graphics2D g2d, BlockPiece piece, int gridRow, int gridCol) {
        boolean valid = game.getBoard().isValidPlacement(piece, gridRow, gridCol);
        int cellSize = getCellSize();
        Composite original = g2d.getComposite();
        for (Cell cell : piece.getCells()) {
            int r = gridRow + cell.getRow();
            int c = gridCol + cell.getCol();
            if (r < 0 || r >= Board.GRID_SIZE || c < 0 || c >= Board.GRID_SIZE) {
                continue;
            }
            int x = getBoardOriginX() + c * cellSize;
            int y = getBoardOriginY() + r * cellSize;
            if (valid) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
                drawBlock(g2d, x, y, cellSize, piece.getColor());
                g2d.setComposite(original);
            } else {
                g2d.setColor(INVALID_PREVIEW);
                g2d.fillRoundRect(x + 2, y + 2, cellSize - 4, cellSize - 4, 8, 8);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(x + 3, y + 3, cellSize - 6, cellSize - 6, 8, 8);
                g2d.drawLine(x + 12, y + 12, x + cellSize - 12, y + cellSize - 12);
                g2d.drawLine(x + cellSize - 12, y + 12, x + 12, y + cellSize - 12);
                g2d.setStroke(new BasicStroke(1f));
            }
        }
        g2d.setComposite(original);
    }

    private void drawBoard(Graphics2D g2d) {
        int cellSize = getCellSize();
        int originX = getBoardOriginX();
        int originY = getBoardOriginY();
        int boardPx = cellSize * Board.GRID_SIZE;

        g2d.setColor(BOARD_BACKGROUND);
        g2d.fillRoundRect(originX - 8, originY - 8, boardPx + 16, boardPx + 16, 18, 18);

        Board board = game.getBoard();
        for (int r = 0; r < Board.GRID_SIZE; r++) {
            for (int c = 0; c < Board.GRID_SIZE; c++) {
                int x = originX + c * cellSize;
                int y = originY + r * cellSize;
                Cell cell = board.getCell(r, c);
                if (cell != null && cell.isOccupied()) {
                    drawBlock(g2d, x, y, cellSize, cell.getColor());
                } else {
                    g2d.setColor(EMPTY_CELL);
                    g2d.fillRoundRect(x + 2, y + 2, cellSize - 4, cellSize - 4, 8, 8);
                }
            }
        }
    }

    private void drawTray(Graphics2D g2d) {
        Tray tray = game.getTray();
        BufferedImage slotImage = UIButtonRenderer.getImage(SLOT_IMAGE);
        int slotHeight = TRAY_AREA_HEIGHT - TRAY_BOTTOM_MARGIN;
        int slotY = getTrayTop();

        for (int i = 0; i < Tray.SLOT_COUNT; i++) {
            int slotX = getSlotX(i);
            if (slotImage != null) {
                g2d.drawImage(slotImage, slotX, slotY, TRAY_SLOT_WIDTH, slotHeight, null);
            } else {
                g2d.setColor(BOARD_BACKGROUND);
                g2d.fillRoundRect(slotX, slotY, TRAY_SLOT_WIDTH, slotHeight, 14, 14);
            }

            if (i == tray.getSelectedSlotIndex()) {
                g2d.setColor(SELECTED_SLOT);
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(slotX, slotY, TRAY_SLOT_WIDTH, slotHeight, 14, 14);
                g2d.setStroke(new BasicStroke(1f));
            }

            g2d.setFont(UIButtonRenderer.getFont(Font.BOLD, 14f));
            g2d.setColor(HUD_LABEL);
            g2d.drawString(String.valueOf(i + 1), slotX + 8, slotY + 18);

            BlockPiece piece = tray.getPiece(i);
            if (piece != null && i != mouseHandler.getDraggedSlot()) {
                drawPieceCentered(g2d, piece, slotX, slotY, TRAY_SLOT_WIDTH, slotHeight);
            }
        }
    }

    private void drawPieceCentered(Graphics2D g2d, BlockPiece piece, int areaX, int areaY, int areaW, int areaH) {
        int rows = getPieceRows(piece);
        int cols = getPieceCols(piece);
        int miniCell = Math.min(24, Math.min((areaW - 20) / cols, (areaH - 20) / rows));
        int startX = areaX + (areaW - cols * miniCell) / 2;
        int startY = areaY + (areaH - rows * miniCell) / 2;

        for (Cell cell : piece.getCells()) {
            drawBlock(g2d, startX + cell.getCol() * miniCell, startY + cell.getRow() * miniCell, miniCell, piece.getColor());
        }
    }

    private void drawHUD(Graphics2D g2d) {
        int w = getWidth();
        ScoreManager scoreManager = game.getScoreManager();
        int current = game.getCurrentScore();
        int best = scoreManager != null ? Math.max(scoreManager.getBestScore(), current) : current;

        g2d.setFont(UIButtonRenderer.getFont(Font.BOLD, 13f));
        g2d.setColor(HUD_LABEL);
        g2d.drawString("SCORE", 30, 30);
        g2d.drawString("BEST", w - 130, 30);
        drawCentered(g2d, game.getGameMode() == GameMode.TIME_ATTACK ? "TIME LEFT" : "TIME", w / 2, 30);

        g2d.setFont(UIButtonRenderer.getFont(Font.BOLD, 26f));
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(current), 30, 60);
        drawCentered(g2d, formatTime(getDisplayedTimeMs()), w / 2, 60);

        BufferedImage star = UIButtonRenderer.getImage(STAR_IMAGE);
        if (star != null) {
            g2d.drawImage(star, w - 130, 38, 24, 24, null);
        }
        g2d.setColor(SELECTED_SLOT);
        g2d.drawString(String.valueOf(best), w - 100, 60);

        g2d.setFont(UIButtonRenderer.getFont(Font.PLAIN, 12f));
        g2d.setColor(HUD_LABEL);
        String status = "P: Pause   M: Sound " + (SoundManager.getInstance().isMuted() ? "OFF" : "ON");
        if (game.getGameMode() == GameMode.TIME_ATTACK) {
            status = "Target " + Game.TIME_ATTACK_TARGET_SCORE + "   " + status;
        }
        drawCentered(g2d, status, w / 2, 82);
    }

    private void drawBlock(Graphics2D g2d, int x, int y, int size, Color color) {
        Color base = color != null ? color : Color.GRAY;
        int arc = Math.max(4, size / 5);
        g2d.setColor(base);
        g2d.fillRoundRect(x + 1, y + 1, size - 2, size - 2, arc, arc);
        g2d.setColor(base.brighter());
        g2d.fillRoundRect(x + 4, y + 3, size - 8, Math.max(2, size / 6), arc, arc);
        g2d.setColor(base.darker());
        g2d.drawRoundRect(x + 1, y + 1, size - 2, size - 2, arc, arc);
    }

    private void drawCentered(Graphics2D g2d, String text, int centerX, int baselineY) {
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(text, centerX - fm.stringWidth(text) / 2, baselineY);
    }

    private long getDisplayedTimeMs() {
        return game.getGameMode() == GameMode.TIME_ATTACK ? game.getRemainingTimeMs() : game.getElapsedTimeMs();
    }

    private static String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    public static int getPieceRows(BlockPiece piece) {
        int max = 0;
        for (Cell cell : piece.getCells()) {
            max = Math.max(max, cell.getRow());
        }
        return max + 1;
    }

    public static int getPieceCols(BlockPiece piece) {
        int max = 0;
        for (Cell cell : piece.getCells()) {
            max = Math.max(max, cell.getCol());
        }
        return max + 1;
    }

    public int getCellSize() {
        int availableW = getWidth() - 2 * BOARD_MARGIN;
        int availableH = getHeight() - HUD_HEIGHT - TRAY_AREA_HEIGHT - 2 * BOARD_MARGIN;
        return Math.max(10, Math.min(availableW, availableH) / Board.GRID_SIZE);
    }

    public int getBoardOriginX() {
        return (getWidth() - getCellSize() * Board.GRID_SIZE) / 2;
    }

    public int getBoardOriginY() {
        return HUD_HEIGHT + BOARD_MARGIN;
    }

    public int getTrayTop() {
        return getHeight() - TRAY_AREA_HEIGHT;
    }

    public int getSlotX(int slotIndex) {
        int totalW = Tray.SLOT_COUNT * TRAY_SLOT_WIDTH + (Tray.SLOT_COUNT - 1) * TRAY_SLOT_GAP;
        return (getWidth() - totalW) / 2 + slotIndex * (TRAY_SLOT_WIDTH + TRAY_SLOT_GAP);
    }

    public Game getGame() {
        return game;
    }

    public GameFrame getFrame() {
        return frame;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}
