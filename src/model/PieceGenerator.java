package model;

import enums.*;
import exceptions.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Factory class responsible for constructing polyomino puzzle pieces
 * and generating random pieces for the gameplay tray.
 */
public class PieceGenerator {

    private static final Random RANDOM = new Random();

    // Vibrant, cohesive color palette for block categories
    private static final Color COLOR_SINGLE = new Color(255, 179, 0);     // Amber
    private static final Color COLOR_LINE_2 = new Color(76, 175, 80);      // Green
    private static final Color COLOR_LINE_3 = new Color(3, 169, 244);      // Sky Blue
    private static final Color COLOR_LINE_4 = new Color(33, 150, 243);     // Deep Blue
    private static final Color COLOR_SQUARE = new Color(255, 214, 0);      // Golden Yellow
    private static final Color COLOR_RECT = new Color(233, 30, 99);        // Magenta / Rose
    private static final Color COLOR_L_SHAPE = new Color(255, 112, 67);    // Coral Orange
    private static final Color COLOR_T_SHAPE = new Color(156, 39, 176);    // Purple
    private static final Color COLOR_S_SHAPE = new Color(0, 188, 212);     // Cyan / Teal

    /**
     * Private constructor to prevent instantiation of this static factory.
     */
    private PieceGenerator() {
    }

    /**
     * Generates a random BlockPiece selected uniformly from all available shape types.
     *
     * @return a freshly created random BlockPiece
     * @throws InvalidPieceException if piece construction fails
     */
    public static BlockPiece getRandomPiece() throws InvalidPieceException {
        ShapeType[] allShapes = ShapeType.values();
        int randomIndex = RANDOM.nextInt(allShapes.length);
        ShapeType selectedShape = allShapes[randomIndex];
        return createPiece(selectedShape);
    }

    /**
     * Creates a BlockPiece instance corresponding to the specified ShapeType.
     *
     * @param shapeType the requested shape configuration
     * @return a new BlockPiece containing occupied relative cells and theme color
     * @throws InvalidPieceException if shapeType is null or unknown
     */
    public static BlockPiece createPiece(ShapeType shapeType) throws InvalidPieceException {
        if (shapeType == null) {
            throw new InvalidPieceException("ShapeType cannot be null.");
        }

        int[][] coordinates = getCoordinatesForShape(shapeType);
        Color color = getColorForShape(shapeType);
        List<Cell> cells = buildCells(coordinates, color);

        return new BlockPiece(shapeType, color, cells);
    }

    /**
     * Converts coordinate offset pairs into a list of occupied Cell objects.
     *
     * @param coordinates array of [row, col] pairs
     * @param color the color to assign each cell
     * @return list of configured Cell instances
     */
    private static List<Cell> buildCells(int[][] coordinates, Color color) {
        List<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < coordinates.length; i++) {
            int row = coordinates[i][0];
            int col = coordinates[i][1];
            cells.add(new Cell(row, col, color));
        }
        return cells;
    }

    /**
     * Maps each ShapeType to its relative coordinate offsets.
     *
     * @param shapeType the shape to look up
     * @return 2D array of [row, col] pairs relative to (0, 0)
     * @throws InvalidPieceException if the shape is unrecognized
     */
    private static int[][] getCoordinatesForShape(ShapeType shapeType) throws InvalidPieceException {
        switch (shapeType) {
            case SINGLE_1X1:
                return new int[][] {
                    {0, 0}
                };

            case LINE_1X2:
                return new int[][] {
                    {0, 0}, {0, 1}
                };

            case LINE_2X1:
                return new int[][] {
                    {0, 0},
                    {1, 0}
                };

            case LINE_1X3:
                return new int[][] {
                    {0, 0}, {0, 1}, {0, 2}
                };

            case LINE_3X1:
                return new int[][] {
                    {0, 0},
                    {1, 0},
                    {2, 0}
                };

            case LINE_1X4:
                return new int[][] {
                    {0, 0}, {0, 1}, {0, 2}, {0, 3}
                };

            case LINE_4X1:
                return new int[][] {
                    {0, 0},
                    {1, 0},
                    {2, 0},
                    {3, 0}
                };

            case SQUARE_2X2:
                return new int[][] {
                    {0, 0}, {0, 1},
                    {1, 0}, {1, 1}
                };

            case RECT_3X2:
                return new int[][] {
                    {0, 0}, {0, 1},
                    {1, 0}, {1, 1},
                    {2, 0}, {2, 1}
                };

            case RECT_2X3:
                return new int[][] {
                    {0, 0}, {0, 1}, {0, 2},
                    {1, 0}, {1, 1}, {1, 2}
                };

            case L_BOTTOM_LEFT:
                return new int[][] {
                    {0, 0},
                    {1, 0},
                    {2, 0}, {2, 1}
                };

            case L_BOTTOM_RIGHT:
                return new int[][] {
                    {0, 1},
                    {1, 1},
                    {2, 0}, {2, 1}
                };

            case L_TOP_LEFT:
                return new int[][] {
                    {0, 0}, {0, 1},
                    {1, 0},
                    {2, 0}
                };

            case L_TOP_RIGHT:
                return new int[][] {
                    {0, 0}, {0, 1},
                    {1, 1},
                    {2, 1}
                };

            case T_TOP:
                return new int[][] {
                    {0, 1},
                    {1, 0}, {1, 1}, {1, 2}
                };

            case T_DOWN:
                return new int[][] {
                    {0, 0}, {0, 1}, {0, 2},
                    {1, 1}
                };

            case T_LEFT:
                return new int[][] {
                    {0, 1},
                    {1, 0}, {1, 1},
                    {2, 1}
                };

            case T_RIGHT:
                return new int[][] {
                    {0, 0},
                    {1, 0}, {1, 1},
                    {2, 0}
                };

            case S_HORIZONTAL:
                return new int[][] {
                    {0, 1}, {0, 2},
                    {1, 0}, {1, 1}
                };

            case S_VERTICAL:
                return new int[][] {
                    {0, 0},
                    {1, 0}, {1, 1},
                    {2, 1}
                };

            default:
                throw new InvalidPieceException("Unknown shape type: " + shapeType);
        }
    }

    /**
     * Determines the display color corresponding to a ShapeType.
     *
     * @param shapeType the target shape
     * @return the associated Color
     */
    private static Color getColorForShape(ShapeType shapeType) {
        switch (shapeType) {
            case SINGLE_1X1:
                return COLOR_SINGLE;

            case LINE_1X2:
            case LINE_2X1:
                return COLOR_LINE_2;

            case LINE_1X3:
            case LINE_3X1:
                return COLOR_LINE_3;

            case LINE_1X4:
            case LINE_4X1:
                return COLOR_LINE_4;

            case SQUARE_2X2:
                return COLOR_SQUARE;

            case RECT_3X2:
            case RECT_2X3:
                return COLOR_RECT;

            case L_BOTTOM_LEFT:
            case L_BOTTOM_RIGHT:
            case L_TOP_LEFT:
            case L_TOP_RIGHT:
                return COLOR_L_SHAPE;

            case T_TOP:
            case T_DOWN:
            case T_LEFT:
            case T_RIGHT:
                return COLOR_T_SHAPE;

            case S_HORIZONTAL:
            case S_VERTICAL:
                return COLOR_S_SHAPE;

            default:
                return Color.GRAY;
        }
    }
}
