package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    // Remember that chess positions start w/ 1 while arrays start at index 0
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        // 1. Check for reference equality
        if (this == o) return true;
        // 2. Check for null and ensure the classes match
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Cast and compare field values
        ChessMove that = (ChessMove) o;
        if (that.startPosition.equals(startPosition) && that.endPosition.equals(endPosition) && that.promotionPiece.equals(promotionPiece)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Move: " + startPosition + "->" + endPosition + " Promotion Type: " + promotionPiece;
    }
}
