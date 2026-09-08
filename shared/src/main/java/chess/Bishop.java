package chess;

import java.util.Collection;
import java.util.HashSet;

public class Bishop extends ChessPiece {
    private HashSet<ChessMove> pieceSpecificMoves(HashSet<ChessMove> moveOptions, ChessBoard board, ChessPosition myPosition) {
        int startingRow = myPosition.getRow();
        int startingColumn = myPosition.getColumn();
        ChessPosition northWestPosition = new ChessPosition(startingRow + 1, startingColumn - 1);
        while (isValidBishopMove(board, northWestPosition)) {
            moveOptions.add(northWestPosition);
        }
        return moveOptions;
    }

    private boolean PieceMoveType(ChessBoard board, ChessPosition position) {
        ChessPiece spaceOccupant = board.getPiece(position);
        if (!position.isValidPosition() || spaceOccupant.getTeamColor() == this.getTeamColor()) return PieceMoveType.INVALID;
        else if (spaceOccupant == null) return PieceMoveType.EMPTY;
        else if (spaceOccupant.getTeamColor() != this.getTeamColor()) return PieceMoveType.TAKE;
    }
}
