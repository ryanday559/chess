package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface MoveStrategy {
    default int[][] getLoopMoveOffsets() {
        int[][] loopMoveOffsets = {};
        return loopMoveOffsets;
    }

    default int[][] getSingleMoveOffsets() {
        int[][] singleMoveOffsets = {};
        return singleMoveOffsets;
    }

    default Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board, ChessPiece.PieceType piece) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves = addLoopMoves(position, board, validMoves, piece);
        validMoves = addSingleMoves(position, board, validMoves, piece);
        return validMoves;
    }

    default Collection<ChessMove> addLoopMoves(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        for (int[] offset : getLoopMoveOffsets()){
            int nextRow = startingColumn + offset[0];
            int nextColumn = startingRow + offset[1];
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            while (board.canMove(position, nextPosition)) {
                ChessMove move = new ChessMove(position, nextPosition, piece);
                validMoves.add(move);
                nextRow += offset[0];
                nextColumn += offset[1];
                nextPosition = new ChessPosition(nextRow, nextColumn);
            }
        }
        return validMoves;
    }

    default Collection<ChessMove> addSingleMoves(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        for (int[] offset : getSingleMoveOffsets()) {
            int nextRow = startingColumn + offset[0];
            int nextColumn = startingRow + offset[1];
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            if (board.canMove(position, nextPosition)) {
                ChessMove move = new ChessMove(position, nextPosition, piece);
                validMoves.add(move);
            }
        }
        return validMoves;
    }

}
