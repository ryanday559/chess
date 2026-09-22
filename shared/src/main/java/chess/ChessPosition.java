package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;


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


    private boolean checkEqualPosition(ChessPosition otherPosition) {
        if (getRow() == otherPosition.getRow() && getColumn() == otherPosition.getColumn()) {
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return checkEqualPosition(that);
    }


    @Override
    public int hashCode() {
        return 31 * Objects.hash(getRow(), getColumn());
    }


    @Override
    public String toString() {
        return "(" + Objects.toString(getRow()) + "," + Objects.toString(getColumn()) + ")";
    }
}
