package chess;
import java.util.ArrayList;
import java.util.List;

interface MoveStrategy {
    int[][] loopMoveOffsets = {};
    int[][] singleMoveOffsets = {};

    default List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board) {
        List<ChessPosition> validMoves = new ArrayList<>();
        validMoves = addLoopMoves(position, board, validMoves);
        validMoves = addSingleMoves(position, board, validMoves);
        return validMoves;
    }

    default List<ChessPosition> addLoopMoves(ChessPosition position, ChessBoard board, List<ChessPosition> validMoves) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        for (int[] offset : loopMoveOffsets){
            int nextRow = startingColumn + offset[0];
            int nextColumn = startingRow + offset[1];
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            while (board.canMove(position, nextPosition)) {
                validMoves.add(nextPosition);
                nextRow += offset[0];
                nextColumn += offset[1];
                nextPosition = new ChessPosition(nextRow, nextColumn);
            }
        }
        return validMoves;
    }

    default List<ChessPosition> addSingleMoves(ChessPosition position, ChessBoard board, List<ChessPosition> validMoves) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        for (int[] offset : singleMoveOffsets) {
            int nextRow = startingColumn + offset[0];
            int nextColumn = startingRow + offset[1];
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            if (board.canMove(position, nextPosition)) {
                validMoves.add(nextPosition);
            }
        }
        return validMoves;
    }

}
