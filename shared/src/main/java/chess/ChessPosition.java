package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;


    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    public boolean isInBounds() {
        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(row) + "," + String.valueOf(col) + ")";
    }


    @Override
    public boolean equals(Object o) {
        // 1. Check for reference equality
        if (this == o) return true;
        // 2. Check for null and ensure the classes match
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Cast and compare field values
        ChessPosition that = (ChessPosition) o;
        if (that.row == row && that.col == col) {
            return true;
        }
        return false;
    }
}
