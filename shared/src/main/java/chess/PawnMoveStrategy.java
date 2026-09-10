package chess;
import java.util.ArrayList;
import java.util.List;

class PawnMoveStrategy implements MoveStrategy {
    // N, S, E, W
    @Override
    public int[][] getSingleMoveOffsets() {
        int[][] loopMoveOffsets = {{1,0}};
        return loopMoveOffsets;
    }


    @Override
    public List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board) {
        List<ChessPosition> validMoves = new ArrayList<>();
        validMoves = MoveStrategy.super.addSingleMoves(position, board);
        validMoves = checkFirstMove(position, board, validMoves);

        return validMoves;
    }


    private List<ChessPosition> checkFirstMove(ChessPosition position, ChessBoard board, List<ChessPosition> validMoves) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        if (board.canMove(position) && startingRow == 2) {
            ChessPosition newPosition = new ChessPosition(startingRow + 2, startingColumn);
            validMoves.add(newPosition);
        }
        return validMoves;
    }


    private List<ChessPosition> checkDiagonalMove(ChessPosition position, ChessBoard board, List<ChessPosition> validMoves) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        ChessPosition upperLeft = new ChessPosition(startingRow + 1, startingColumn - 1);
        ChessPosition upperRight = new ChessPosition(startingRow + 1, startingColumn + 1);
        if (board.canTake(position, upperLeft)) {
            validMoves.add(upperLeft);
        }
        if (board.canTake(position, upperRight)) {
            validMoves.add(upperRight);
        }
        return validMoves;
    }


}