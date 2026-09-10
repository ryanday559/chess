package chess;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class BishopMoveStrategy implements MoveStrategy {
    private List<ChessPosition> validMoves = new ArrayList<>();
    private int[][] offsets = {{1,-1}, {1,1}, {-1,1}, {-1,-1}};


    @Override
    public List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board){
        // We need to check NW, NE, SE, and SW
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        for (int[] offset : offsets){
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

}
