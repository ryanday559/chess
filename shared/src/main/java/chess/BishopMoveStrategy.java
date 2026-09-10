package chess;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class BishopMoveStrategy implements MoveStrategy {
    private List<ChessPosition> validMoves = new ArrayList<>();

    @Override
    public List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board){
        // We need to check NW, NE, SE, and SW
        checkNwMoves(position, board);
        checkNeMoves(position, board);
        checkSwMoves(position, board);
        checkSeMoves(position, board);
        return validMoves;
    }

    private void checkNwMoves(ChessPosition startPosition, ChessBoard board) {
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();
        int nextRow = startRow + 1;
        int nextColumn = startColumn - 1;
        ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
        while(board.canMove(startPosition, nextPosition)) {
            validMoves.add(nextPosition);
            nextRow += 1;
            nextColumn -= 1;
            nextPosition = new ChessPosition(nextRow, nextColumn);
        }
    }

    private void checkNeMoves(ChessPosition startPosition, ChessBoard board) {
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();
        int nextRow = startRow + 1;
        int nextColumn = startColumn + 1;
        ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
        while(board.canMove(startPosition, nextPosition)) {
            validMoves.add(nextPosition);
            nextRow += 1;
            nextColumn += 1;
            nextPosition = new ChessPosition(nextRow, nextColumn);
        }
    }

    private void checkSwMoves(ChessPosition startPosition, ChessBoard board) {
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();
        int nextRow = startRow - 1;
        int nextColumn = startColumn - 1;
        ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
        while(board.canMove(startPosition, nextPosition)) {
            validMoves.add(nextPosition);
            nextRow -= 1;
            nextColumn -= 1;
            nextPosition = new ChessPosition(nextRow, nextColumn);
        }
    }

    private void checkSeMoves(ChessPosition startPosition, ChessBoard board) {
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();
        int nextRow = startRow - 1;
        int nextColumn = startColumn + 1;
        ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
        while(board.canMove(startPosition, nextPosition)) {
            validMoves.add(nextPosition);
            nextRow -= 1;
            nextColumn += 1;
            nextPosition = new ChessPosition(nextRow, nextColumn);
        }
    }
}
