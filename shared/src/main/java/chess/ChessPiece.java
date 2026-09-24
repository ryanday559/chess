package chess;

import java.util.ArrayList;
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
    private ChessGame.TeamColor pieceColor;
    private PieceType type;
    private boolean hasMoved = false;
    private boolean enPassantVulnerable = false;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    public boolean checkHasMoved() {
        return hasMoved;
    }


    public void setHasMoved(boolean moved) {
        hasMoved = moved;
    }


    public void setEnPassantVulnerable(boolean vulnerable) {
        enPassantVulnerable = vulnerable;
    }


    public boolean getEnPassantVulnerable() {
        return enPassantVulnerable;
    }


    private Map<PieceType, PieceMovement> movementRuleMap = Map.of(
            PieceType.BISHOP, new BishopMovement(),
            PieceType.ROOK, new RookMovement(),
            PieceType.KNIGHT, new KnightMovement(),
            PieceType.KING, new KingMovement(),
            PieceType.QUEEN, new QueenMovement(),
            PieceType.PAWN, new PawnMovement()
    );


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null) {
            return new ArrayList<ChessMove>();
        }
        ChessPiece.PieceType type = piece.getPieceType();
        PieceMovement movementRules = movementRuleMap.get(type);
        return movementRules.getMoveOptions(board, myPosition);
    }


    private Map<PieceType, String> pieceStringMap = Map.of(
            PieceType.QUEEN, "Q",
            PieceType.KING, "K",
            PieceType.PAWN, "P",
            PieceType.BISHOP, "B",
            PieceType.ROOK, "R",
            PieceType.KNIGHT, "N"
    );


    private boolean checkEqualPiece(ChessPiece otherPiece) {
        if (
            getPieceType() == otherPiece.getPieceType() &&
            getTeamColor() == otherPiece.getTeamColor() &&
            checkHasMoved() == otherPiece.checkHasMoved()
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
        ChessPiece that = (ChessPiece) o;
        return checkEqualPiece(that);
    }


    @Override
    public int hashCode() {
    return 31 * Objects.hash(pieceColor, type, hasMoved);
    }


    @Override
    public String toString() {
        String pieceString = pieceStringMap.get(getPieceType());
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            return pieceString.toLowerCase();
        }
        return pieceString;
    }
}
