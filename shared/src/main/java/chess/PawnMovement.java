package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovement implements PieceMovement{
    ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;


    @Override
    public int[][] getSingleOffsets() {
        if (color == ChessGame.TeamColor.WHITE) {
            int[][] offsets = {
                    {1,0}
            };
            return offsets;
        }
        int[][] offsets = {
                {-1,0}
        };
        return offsets;
    }


    private Collection<ChessMove> addMoveWithPromotionCheck(ChessPosition startPosition, ChessPosition endPosition, Collection<ChessMove> moveOptions) {
        if (
            (color == ChessGame.TeamColor.WHITE && endPosition.getRow() != 8) ||
            (color == ChessGame.TeamColor.BLACK && endPosition.getRow() != 1)
        ) {
            moveOptions.add(new ChessMove(startPosition, endPosition, null));
            return moveOptions;
        }
        moveOptions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        moveOptions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moveOptions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        moveOptions.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        return moveOptions;
    }


    @Override
    public Collection<ChessMove> getSingleMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moveOptions) {
        int[][] offsets = getSingleOffsets();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        for (int[] offset : offsets) {
            ChessPosition newPosition = new ChessPosition(startRow + offset[0], startCol + offset[1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            if (board.getPiece(newPosition) == null && !board.moveOutOfBounds(newMove)) {
                moveOptions = addMoveWithPromotionCheck(myPosition, newPosition, moveOptions);
            }
        }
        return moveOptions;
    }


    public Collection<ChessMove> getPawnStartingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moveOptions) {
        if (
            (color == ChessGame.TeamColor.WHITE && myPosition.getRow() != 2) ||
            (color == ChessGame.TeamColor.BLACK && myPosition.getRow() != 7)
        ) {
            return moveOptions;
        }
        int[][] baseOffsets = getSingleOffsets();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        for (int[] offset : baseOffsets) {
            ChessPosition blockedPosition = new ChessPosition(startRow + offset[0], startCol + offset[1]);
            if (board.getPiece(blockedPosition) != null) {
                return moveOptions;
            }
            ChessPosition newPosition = new ChessPosition(startRow + (offset[0] * 2), startCol + (offset[1] * 2));
            if (board.getPiece(newPosition) == null) {
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveOptions.add(newMove);
            }
        }
        return moveOptions;
    }


    public int[][] getPawnCaptureOffsets() {
        if (color == ChessGame.TeamColor.WHITE) {
            int[][] offsets = {
                    {1,1},
                    {1,-1}
            };
            return offsets;
        }
        int[][] offsets = {
                {-1,1},
                {-1,-1}
        };
        return offsets;
    }


    public Collection<ChessMove> getPawnCaptureMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moveOptions) {
        int[][] offsets = getPawnCaptureOffsets();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        for (int[] offset : offsets) {
            ChessPosition newPosition = new ChessPosition(startRow + offset[0], startCol + offset[1]);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            if (!board.moveOutOfBounds(newMove) && board.canCapture(myPosition, newPosition)) {
                moveOptions = addMoveWithPromotionCheck(myPosition, newPosition, moveOptions);
            }
        }
        return moveOptions;
    }


    @Override
    public Collection<ChessMove> getMoveOptions(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveOptions = new ArrayList<ChessMove>();
        color = board.getPiece(myPosition).getTeamColor();
        moveOptions = getSingleMoves(board, myPosition, moveOptions);
        moveOptions = getPawnStartingMoves(board, myPosition, moveOptions);
        moveOptions = getPawnCaptureMoves(board, myPosition, moveOptions);
        return moveOptions;
    }
}
