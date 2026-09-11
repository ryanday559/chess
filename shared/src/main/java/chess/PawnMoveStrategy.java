package chess;
import java.util.ArrayList;
import java.util.Collection;

class PawnMoveStrategy implements MoveStrategy {
    // N, S

    public int[][] getWhiteMoveOffsets() {
        int[][] loopMoveOffsets = {{1,0}};
        return loopMoveOffsets;
    }


    public int[][] getBlackMoveOffsets() {
        int[][] loopMoveOffsets = {{-1,0}};
        return loopMoveOffsets;
    }


    @Override
    public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board, ChessPiece.PieceType piece) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves = addSingleMoves(position, board, validMoves, piece);
        validMoves = checkFirstMove(position, board, validMoves, piece);
        validMoves = checkDiagonalMove(position, board, validMoves, piece);
        return validMoves;
    }


    @Override
    public Collection<ChessMove> addSingleMoves(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int[][] moveOffsets;
        ChessGame.TeamColor pawnColor = getPawnColor(position, board);
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            moveOffsets = getWhiteMoveOffsets();
        }
        else {
            moveOffsets = getBlackMoveOffsets();
        }
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        for (int[] offset : moveOffsets) {
            int nextRow = startingRow + offset[0];
            int nextColumn = startingColumn + offset[1];
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            if (board.canMove(position, nextPosition)) {
                ChessMove move = new ChessMove(position, nextPosition, null);
                if (checkPromotion(move, board)) {
                    validMoves = addPromotionOptions(move, validMoves);
                }
                else {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }


    private Collection<ChessMove> checkFirstMove(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        ChessGame.TeamColor pawnColor = getPawnColor(position, board);
        int[] startingOffset = getStartingOffset(pawnColor);
        ChessPosition newPosition = new ChessPosition(startingRow + startingOffset[0], startingColumn);
        if (board.canMove(position, newPosition) && ((startingRow == 2 && pawnColor == ChessGame.TeamColor.WHITE) || (startingRow == 7 && pawnColor == ChessGame.TeamColor.BLACK))) {
            ChessMove move = new ChessMove(position, newPosition, null);
            validMoves.add(move);
        }
        return validMoves;
    }


    private Collection<ChessMove> checkDiagonalMove(ChessPosition position, ChessBoard board, Collection<ChessMove> validMoves, ChessPiece.PieceType piece) {
        int startingRow = position.getRow();
        int startingColumn = position.getColumn();
        ChessGame.TeamColor pawnColor = getPawnColor(position, board);
        int[][] diagonalOffsets = getDiagonalOffsets(pawnColor);
        ChessPosition left = new ChessPosition(startingRow + diagonalOffsets[0][0], startingColumn + diagonalOffsets[0][1]);
        ChessPosition right = new ChessPosition(startingRow + diagonalOffsets[1][0], startingColumn + diagonalOffsets[1][1]);
        if (board.canTake(position, left)) {
            ChessMove move = new ChessMove(position, left, null);
            if (checkPromotion(move, board)) {
                validMoves = addPromotionOptions(move, validMoves);
            }
            else {
                validMoves.add(move);
            }
        }
        if (board.canTake(position, right)) {
            ChessMove move = new ChessMove(position, right, null);
            if (checkPromotion(move, board)) {
                validMoves = addPromotionOptions(move, validMoves);
            }
            else {
                validMoves.add(move);
            }
        }
        return validMoves;
    }


    private ChessGame.TeamColor getPawnColor(ChessPosition position, ChessBoard board) {
        return board.getPiece(position).getTeamColor();
    }


    private int[][] getDiagonalOffsets(ChessGame.TeamColor pawnColor) {
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            return new int[][]{{1, -1}, {1, 1}};
        }
        else {
            return new int[][] {{-1, -1}, {-1, 1}};
        }
    }


    private int[] getStartingOffset(ChessGame.TeamColor pawnColor) {
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            return new int[] {2, 0};
        }
        else {
            return new int[] {-2, 0};
        }
    }


    private boolean checkPromotion(ChessMove move, ChessBoard board) {
        int endRow = move.getEndPosition().getRow();
        ChessGame.TeamColor pawnColor = board.getPiece(move.getStartPosition()).getTeamColor();
        if ((pawnColor == ChessGame.TeamColor.WHITE && endRow == 8) || (pawnColor == ChessGame.TeamColor.BLACK && endRow == 1)) return true;
        return false;
    }


    private Collection<ChessMove> addPromotionOptions(ChessMove move, Collection<ChessMove> validMoves) {
        ChessPosition moveStart = move.getStartPosition();
        ChessPosition moveEnd = move.getEndPosition();
        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            if (pieceType == ChessPiece.PieceType.KING || pieceType == ChessPiece.PieceType.PAWN) continue;
            ChessMove newMove = new ChessMove(moveStart, moveEnd, pieceType);
            validMoves.add(newMove);
        }
        return validMoves;
    }

}