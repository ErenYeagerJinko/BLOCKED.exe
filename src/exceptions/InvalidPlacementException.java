package exceptions;

public class InvalidPlacementException extends GameException {
    private int attemptedRow;
    private int attemptedCol;

    public InvalidPlacementException(String message) {
        super(message);
        this.attemptedRow = -1;
        this.attemptedCol = -1;
    }

    public InvalidPlacementException(String message, int row, int col) {
        super(message);
        this.attemptedRow = row;
        this.attemptedCol = col;
    }

    public InvalidPlacementException(String message, int row, int col, Throwable cause) {
        super(message, cause);
        this.attemptedRow = row;
        this.attemptedCol = col;
    }

    public int getAttemptedRow() {
        return attemptedRow;
    }

    public int getAttemptedCol() {
        return attemptedCol;
    }
}
