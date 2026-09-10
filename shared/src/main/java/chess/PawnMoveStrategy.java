package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class PawnMoveStrategy implements MoveStrategy {
    // N, S, E, W
    @Override
    public int[][] getSingleMoveOffsets() {
        int[][] loopMoveOffsets = {{1,0}};
        return loopMoveOffsets;
    }


    @Override
    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board, ChessPiece.PieceType piece) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves = MoveStrategy.super.addSingleMoves(position, board, validMoves, piece);
        validMoves = checkFirstMove(position, board, validMoves, piece);

        return validMoves;
    }


    private Collection<ChessMove> checkFirstMove(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        ChessPosition newPosition = new ChessPosition(startingRow + 2, startingColumn);
        if (board.canMove(position, newPosition) && startingRow == 2) {
            ChessMove move = new ChessMove(position, newPosition, piece);
            validMoves.add(move);
        }
        return validMoves;
    }


    private Collection<ChessMove> checkDiagonalMove(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        ChessPosition upperLeft = new ChessPosition(startingRow + 1, startingColumn - 1);
        ChessPosition upperRight = new ChessPosition(startingRow + 1, startingColumn + 1);
        if (board.canTake(position, upperLeft)) {
            ChessMove move = new ChessMove(position, upperLeft, piece);
            validMoves.add(move);
        }
        if (board.canTake(position, upperRight)) {
            ChessMove move = new ChessMove(position, upperRight, piece);
            validMoves.add(move);
        }
        return validMoves;
    }


}