package animation;

import model.*;
import util.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Visual animation that renders an elastic pop/bounce and subtle glow
 * when a block piece is placed onto the board grid.
 */
public class PlacementPopAnimation extends Animation {

    private static final long DEFAULT_DURATION_MS = 250;
    private static final float MAX_POP_SCALE = 0.20f;

    private final List<Cell> placedCells;
    private final Color pieceColor;
    private final int boardOriginX;
    private final int boardOriginY;
    private final int cellSize;

    /**
     * Constructs a PlacementPopAnimation with custom duration.
     *
     * @param piece the BlockPiece that was placed
     * @param gridRow the board row where the piece anchor was dropped
     * @param gridCol the board col where the piece anchor was dropped
     * @param originX board pixel X origin
     * @param originY board pixel Y origin
     * @param cellSize pixel width/height of each grid cell
     * @param durationMs duration of the pop animation in milliseconds
     */
    public PlacementPopAnimation(BlockPiece piece, int gridRow, int gridCol, int originX, int originY, int cellSize, long durationMs) {
        super(durationMs);
        this.boardOriginX = originX;
        this.boardOriginY = originY;
        this.cellSize = cellSize;
        this.placedCells = new ArrayList<Cell>();

        if (piece != null) {
            Color color = piece.getColor();
            if (color != null) {
                this.pieceColor = color;
            } else {
                this.pieceColor = new Color(255, 179, 0);
            }

            List<Cell> cells = piece.getCells();
            if (cells != null) {
                for (int i = 0; i < cells.size(); i++) {
                    Cell c = cells.get(i);
                    int absoluteRow = gridRow + c.getRow();
                    int absoluteCol = gridCol + c.getCol();
                    this.placedCells.add(new Cell(absoluteRow, absoluteCol, this.pieceColor));
                }
            }
        } else {
            this.pieceColor = new Color(255, 179, 0);
        }

        Logger.getLogger().log("PlacementPopAnimation started for " + this.placedCells.size() + " cells (" + durationMs + "ms)");
    }

    /**
     * Constructs a PlacementPopAnimation with default duration (250ms).
     *
     * @param piece the BlockPiece that was placed
     * @param gridRow the board row where the piece anchor was dropped
     * @param gridCol the board col where the piece anchor was dropped
     * @param originX board pixel X origin
     * @param originY board pixel Y origin
     * @param cellSize pixel width/height of each grid cell
     */
    public PlacementPopAnimation(BlockPiece piece, int gridRow, int gridCol, int originX, int originY, int cellSize) {
        this(piece, gridRow, gridCol, originX, originY, cellSize, DEFAULT_DURATION_MS);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (finished) {
            return;
        }
        if (g2d == null) {
            return;
        }
        if (placedCells.isEmpty()) {
            return;
        }

        float progress = getProgress();
        float scale = 1.0f + (float) Math.sin(progress * Math.PI) * MAX_POP_SCALE;

        int glowAlpha = (int) ((1.0f - progress) * 160);
        if (glowAlpha < 0) {
            glowAlpha = 0;
        }
        if (glowAlpha > 255) {
            glowAlpha = 255;
        }

        Object oldAntialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Composite originalComposite = g2d.getComposite();

        int scaledSize = (int) (cellSize * scale);
        int halfScaled = scaledSize / 2;
        int halfCell = cellSize / 2;
        int cornerRadius = Math.max(4, cellSize / 5);

        for (int i = 0; i < placedCells.size(); i++) {
            Cell cell = placedCells.get(i);
            int centerX = boardOriginX + cell.getCol() * cellSize + halfCell;
            int centerY = boardOriginY + cell.getRow() * cellSize + halfCell;

            int drawX = centerX - halfScaled;
            int drawY = centerY - halfScaled;

            // Draw block body
            g2d.setColor(pieceColor);
            g2d.fillRoundRect(drawX, drawY, scaledSize, scaledSize, cornerRadius, cornerRadius);

            // Draw block outline
            g2d.setColor(pieceColor.brighter());
            g2d.drawRoundRect(drawX, drawY, scaledSize, scaledSize, cornerRadius, cornerRadius);

            // Draw pop flash / glow overlay
            if (glowAlpha > 0) {
                g2d.setColor(new Color(255, 255, 255, glowAlpha));
                g2d.fillRoundRect(drawX, drawY, scaledSize, scaledSize, cornerRadius, cornerRadius);
            }
        }

        g2d.setComposite(originalComposite);
        if (oldAntialias != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
        }
    }

    public List<Cell> getPlacedCells() {
        return new ArrayList<Cell>(placedCells);
    }

    public Color getPieceColor() {
        return pieceColor;
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
