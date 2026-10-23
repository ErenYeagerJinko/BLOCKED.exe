package model;

import java.awt.Color;
import java.util.Objects;

public class Cell {
    private int row;
    private int col;
    private boolean occupied;
    private Color color;

    public Cell(int row, int col) {
        this(row, col, null);
    }

    public Cell(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.occupied = (color != null);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
        if (!occupied) {
            this.color = null;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.occupied = (color != null);
    }

    public void clear() {
        this.occupied = false;
        this.color = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
               col == cell.col &&
               occupied == cell.occupied &&
               Objects.equals(color, cell.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, occupied, color);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", occupied=" + occupied +
                ", color=" + color +
                '}';
    }
}
