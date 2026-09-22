package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovement {
    default int[][] getSingleOffsets() {
        int[][] offsets = {};
        return offsets;
    }


    default int[][] getLoopOffsets() {
        int[][] offsets = {};
        return offsets;
    }


    default Collection<ChessMove> getSingleMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moveOptions) {
        int[][] offsets = getSingleOffsets();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        for (int[] offset : offsets) {
            ChessPosition newPosition = new ChessPosition(startRow + offset[0], startCol + offset[1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            if (board.isValidMove(newMove)) {
                moveOptions.add(newMove);
            }
        }
        return moveOptions;
    }


    default Collection<ChessMove> getLoopMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moveOptions) {
        int[][] offsets = getLoopOffsets();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        for (int[] offset : offsets) {
            int nextRow = startRow + offset[0];
            int nextCol = startCol + offset[1];
            ChessPosition nextPosition = new ChessPosition(nextRow, nextCol);
            ChessMove nextMove = new ChessMove(myPosition, nextPosition, null);
            while (board.isValidMove(nextMove)) {
                moveOptions.add(nextMove);
                if (board.canCapture(myPosition, nextPosition)) {
                    break;
                }
                nextRow += offset[0];
                nextCol += offset[1];
                nextPosition = new ChessPosition(nextRow, nextCol);
                nextMove = new ChessMove(myPosition, nextPosition, null);
            }
        }
        return moveOptions;
    }


    default Collection<ChessMove> getMoveOptions(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveOptions = new ArrayList<ChessMove>();
        moveOptions = getSingleMoves(board, myPosition, moveOptions);
        moveOptions = getLoopMoves(board, myPosition, moveOptions);
        return moveOptions;
    }

}
