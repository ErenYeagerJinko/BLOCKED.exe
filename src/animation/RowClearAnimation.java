package animation;

import util.*;
import java.awt.*;
import java.util.*;

public class RowClearAnimation extends Animation {
    private java.util.List<Integer> rows;
    private java.util.List<Integer> cols;
    private int boardOriginX;
    private int boardOriginY;
    private int cellSize;

    public RowClearAnimation(java.util.List<Integer> rows, java.util.List<Integer> cols, int originX, int originY, int cellSize, long durationMs) {
        super(durationMs);
        this.rows = rows != null ? new ArrayList<>(rows) : new ArrayList<>();
        this.cols = cols != null ? new ArrayList<>(cols) : new ArrayList<>();
        this.boardOriginX = originX;
        this.boardOriginY = originY;
        this.cellSize = cellSize;
        Logger.getLogger().log("RowClearAnimation started for " + this.rows.size() + " rows and " + this.cols.size() + " columns (" + durationMs + "ms)");
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (finished || g2d == null) {
            return;
        }

        float p = getProgress();
        int alpha = Math.max(0, Math.min(255, (int) ((1.0f - p) * 220)));
        if (alpha <= 0) {
            return;
        }

        Composite originalComposite = g2d.getComposite();
        g2d.setColor(new Color(255, 255, 255, alpha));

        int boardWidth = 8 * cellSize;
        int boardHeight = 8 * cellSize;

        for (int r : rows) {
            int ry = boardOriginY + r * cellSize;
            g2d.fillRect(boardOriginX, ry, boardWidth, cellSize);
        }

        for (int c : cols) {
            int cx = boardOriginX + c * cellSize;
            g2d.fillRect(cx, boardOriginY, cellSize, boardHeight);
        }

        int highlightAlpha = Math.min(255, alpha + 35);
        g2d.setColor(new Color(255, 235, 120, highlightAlpha));
        for (int r : rows) {
            for (int c : cols) {
                int cx = boardOriginX + c * cellSize;
                int ry = boardOriginY + r * cellSize;
                g2d.fillRect(cx, ry, cellSize, cellSize);
            }
        }

        g2d.setComposite(originalComposite);
    }

    public java.util.List<Integer> getRows() {
        return new ArrayList<>(rows);
    }

    public java.util.List<Integer> getCols() {
        return new ArrayList<>(cols);
    }

    public int getBoardOriginX() {
        return boardOriginX;
    }

    public int getBoardOriginY() {
        return boardOriginY;
    }

    public int getCellSize() {
        return cellSize;
    }
}
