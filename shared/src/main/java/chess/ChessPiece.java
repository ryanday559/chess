package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

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
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
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
        MoveStrategy pieceMovementRules = movementRules.get(getPieceType());
        return pieceMovementRules.getValidMoves(myPosition, board);
    }

}
