package model;

import enums.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BlockPiece {
    private ShapeType shapeType;
    private Color color;
    private List<Cell> cells;

    public BlockPiece(ShapeType shapeType, Color color, List<Cell> cells) {
        this.shapeType = shapeType;
        this.color = color;
        this.cells = cells;
    }

    public Color getColor() {
        return this.color;
    }

    public ShapeType getShapeType() {
        return this.shapeType;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }
}
