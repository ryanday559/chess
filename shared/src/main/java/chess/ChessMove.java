package chess;

import java.util.Objects;

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


    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
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


    private boolean checkEqualMove(ChessMove otherMove) {
        if (getStartPosition().equals(otherMove.getStartPosition()) &&
            getEndPosition().equals(otherMove.getEndPosition()) &&
            getPromotionPiece() == (otherMove.getPromotionPiece())
        ) {
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
        ChessMove that = (ChessMove) o;
        return checkEqualMove(that);
    }


    @Override
    public int hashCode() {
        return 31 * Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }


    @Override
    public String toString() {
        return "Move[" + getStartPosition().toString() + "->" + getEndPosition().toString() + "]";
    }
}
