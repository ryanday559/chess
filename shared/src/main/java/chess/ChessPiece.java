package chess;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType pieceTypeInstance;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        pieceTypeInstance = type;
    }


    @Override
    public int hashCode() {
        return 31 * Objects.hash(color, pieceTypeInstance);
    }


    private boolean checkEqualPiece(ChessPiece piece1, ChessPiece piece2) {
        if (piece1.color == piece2.color && piece1.pieceTypeInstance == piece2.pieceTypeInstance) {
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        // 1. Check for reference equality
        if (this == o) return true;
        // 2. Check for null and ensure the classes match
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Cast and compare field values
        ChessPiece that = (ChessPiece) o;
        return checkEqualPiece(this, that);
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceTypeInstance;
    }

    public enum PieceMoveType {
        EMPTY,
        INVALID,
        TAKE
    }

    private Map<PieceType, MoveStrategy> movementRules = Map.of(
        PieceType.KING, new KingMoveStrategy(),
        PieceType.QUEEN, new QueenMoveStrategy(),
        PieceType.BISHOP, new BishopMoveStrategy(),
        PieceType.KNIGHT, new KnightMoveStrategy(),
        PieceType.ROOK, new RookMoveStrategy(),
        PieceType.PAWN, new PawnMoveStrategy()
    );

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece.PieceType pieceType = getPieceType();
        var pieceMovementRules = movementRules.get(pieceType);
        return pieceMovementRules.getValidMoves(myPosition, board, pieceType);
    }

}
