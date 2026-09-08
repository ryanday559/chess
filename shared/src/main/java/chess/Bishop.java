package chess;

import java.util.Collection;
import java.util.HashSet;

public class Bishop extends ChessPiece {
    private HashSet<ChessMove> pieceSpecificMoves(HashSet<ChessMove> moveOptions, ChessBoard board, ChessPosition myPosition) {
        int startingRow = myPosition.getRow();
        int startingColumn = myPosition.getColumn();
        int northWestPosition = new ChessPosition(startingRow + 1, startingColumn - 1);
        while (isValidBishopMove())
        return moveOptions;
    }

    private boolean isValidBishopMove(ChessBoard board, ChessPosition position) {
        ChessPiece spaceOccupant = board.getPiece(position);
        if ((spaceOccupant == null || spaceOccupant.getTeamColor() != this.getTeamColor()) && position.isValidPosition()) {
            return true;
        }
        return false;
    }
}
