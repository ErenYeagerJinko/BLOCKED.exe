package model;

import exceptions.*;
import util.*;
import java.awt.*;
import java.util.*;

public class Board {
    public static final int GRID_SIZE = 8;
    private Cell[][] grid;

    public Board() {
        this.grid = new Cell[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                this.grid[r][c] = new Cell(r, c);
            }
        }
        Logger.getLogger().log("Board initialized (" + GRID_SIZE + "x" + GRID_SIZE + ")");
    }

    public void reset() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                this.grid[r][c].clear();
            }
        }
        Logger.getLogger().log("Board reset: all cells cleared");
    }

    public boolean isValidPlacement(BlockPiece piece, int gridRow, int gridCol) {
        if (piece == null || piece.getCells() == null || piece.getCells().isEmpty()) {
            return false;
        }

        for (Cell pieceCell : piece.getCells()) {
            int targetR = gridRow + pieceCell.getRow();
            int targetC = gridCol + pieceCell.getCol();

            if (targetR < 0 || targetR >= GRID_SIZE || targetC < 0 || targetC >= GRID_SIZE) {
                return false;
            }

            if (grid[targetR][targetC].isOccupied()) {
                return false;
            }
        }
        return true;
    }

    public boolean placePiece(BlockPiece piece, int gridRow, int gridCol) throws InvalidPlacementException {
        if (!isValidPlacement(piece, gridRow, gridCol)) {
            Logger.getLogger().error("Invalid piece placement at row=" + gridRow + ", col=" + gridCol);
            throw new InvalidPlacementException("Invalid piece placement at position", gridRow, gridCol);
        }

        Color color = piece.getColor() != null ? piece.getColor() : new Color(60, 140, 240);
        for (Cell pieceCell : piece.getCells()) {
            int targetR = gridRow + pieceCell.getRow();
            int targetC = gridCol + pieceCell.getCol();
            grid[targetR][targetC].setColor(color);
            grid[targetR][targetC].setOccupied(true);
        }
        Logger.getLogger().log("Piece successfully placed on board at (" + gridRow + ", " + gridCol + ")");
        return true;
    }

    public java.util.List<Integer> detectCompletedRows() {
        java.util.List<Integer> rows = new ArrayList<>();
        for (int r = 0; r < GRID_SIZE; r++) {
            boolean full = true;
            for (int c = 0; c < GRID_SIZE; c++) {
                if (!grid[r][c].isOccupied()) {
                    full = false;
                    break;
                }
            }
            if (full) {
                rows.add(r);
            }
        }
        return rows;
    }

    public java.util.List<Integer> detectCompletedColumns() {
        java.util.List<Integer> cols = new ArrayList<>();
        for (int c = 0; c < GRID_SIZE; c++) {
            boolean full = true;
            for (int r = 0; r < GRID_SIZE; r++) {
                if (!grid[r][c].isOccupied()) {
                    full = false;
                    break;
                }
            }
            if (full) {
                cols.add(c);
            }
        }
        return cols;
    }

    public int clearLines(java.util.List<Integer> rows, java.util.List<Integer> cols) {
        Set<Point> cellsToClear = new HashSet<>();

        if (rows != null) {
            for (int r : rows) {
                if (r >= 0 && r < GRID_SIZE) {
                    for (int c = 0; c < GRID_SIZE; c++) {
                        cellsToClear.add(new Point(r, c));
                    }
                }
            }
        }

        if (cols != null) {
            for (int c : cols) {
                if (c >= 0 && c < GRID_SIZE) {
                    for (int r = 0; r < GRID_SIZE; r++) {
                        cellsToClear.add(new Point(r, c));
                    }
                }
            }
        }

        for (Point p : cellsToClear) {
            grid[p.x][p.y].clear();
        }

        if (!cellsToClear.isEmpty()) {
            Logger.getLogger().log("Board cleared " + cellsToClear.size() + " cells across completed lines");
        }

        return cellsToClear.size();
    }

    public boolean canPieceFitAnywhere(BlockPiece piece) {
        if (piece == null) {
            return false;
        }
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (isValidPlacement(piece, r, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Cell getCell(int r, int c) {
        if (r >= 0 && r < GRID_SIZE && c >= 0 && c < GRID_SIZE) {
            return grid[r][c];
        }
        return null;
    }

    public boolean isCellOccupied(int r, int c) {
        Cell cell = getCell(r, c);
        return cell != null && cell.isOccupied();
    }

    public void setCellOccupied(int r, int c, Color color) {
        Cell cell = getCell(r, c);
        if (cell != null) {
            cell.setColor(color);
            cell.setOccupied(color != null);
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
