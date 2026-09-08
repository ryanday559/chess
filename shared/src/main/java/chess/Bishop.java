package chess;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Collection;
import java.util.HashSet;

public class Bishop extends ChessPiece {
    private HashSet<ChessMove> pieceSpecificMoves(HashSet<ChessMove> moveOptions, ChessBoard board, ChessPosition myPosition) {
        moveOptions = checkNorthwestMoves(moveOptions, board, myPosition);

        return moveOptions;
    }

    private HashSet<ChessMove> checkNorthwestMoves(HashSet<ChessMove> moveOptions, ChessBoard board, ChessPosition myPosition) {
        int startingRow = myPosition.getRow();
        int startingColumn = myPosition.getColumn();
        ChessPosition northWestPosition = new ChessPosition(startingRow + 1, startingColumn - 1);
        while (checkPieceMoveType(board, northWestPosition) != PieceMoveType.INVALID) {
            moveOptions.add(northWestPosition);
            startingRow += 1;
            startingColumn -= 1;
            northWestPosition = new ChessPosition(startingRow + 1, startingColumn - 1);
        }
    }

    private boolean checkPieceMoveType(ChessBoard board, ChessPosition position) {
        ChessPiece spaceOccupant = board.getPiece(position);
        if (!position.isValidPosition() || spaceOccupant.getTeamColor() == this.getTeamColor()) return PieceMoveType.INVALID;
        else if (spaceOccupant == null) return PieceMoveType.EMPTY;
        else if (spaceOccupant.getTeamColor() != this.getTeamColor()) return PieceMoveType.TAKE;
    }
}
