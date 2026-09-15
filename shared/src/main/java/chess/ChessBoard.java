package chess;

import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board  = new ChessPiece[8][8];

    public ChessBoard() {

    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // Check position first before I do the below
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        addStandardPieceRow(1, ChessGame.TeamColor.WHITE);
        addPawnRow(2, ChessGame.TeamColor.WHITE);
        addPawnRow(7, ChessGame.TeamColor.BLACK);
        addStandardPieceRow(8, ChessGame.TeamColor.BLACK);
    }


    private void addPawnRow(int row, ChessGame.TeamColor teamColor) {
        for (int i = 0; i < board[row - 1].length; i++) {
            ChessPiece pawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            ChessPosition pawnPosition = new ChessPosition(row, i + 1);
            addPiece(pawnPosition, pawn);
        }
    }


    private void addStandardPieceRow(int row, ChessGame.TeamColor teamColor) {
        ChessPiece.PieceType[] pieceOrder = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        for (int i = 0; i < pieceOrder.length; i++) {
            ChessPiece newPiece = new ChessPiece(teamColor, pieceOrder[i]);
            ChessPosition piecePosition = new ChessPosition(row, i + 1);
            addPiece(piecePosition, newPiece);
        }
    }


    public boolean canTake(ChessPosition startingPosition, ChessPosition finalPosition) {
        // Check the starting position piece then check the final position piece and compare teams
        if (!finalPosition.isInBounds() || getPiece(finalPosition) == null) return false;
        ChessGame.TeamColor attackingColor = getPiece(startingPosition).getTeamColor();
        ChessGame.TeamColor defendingColor = getPiece(finalPosition).getTeamColor();
        if (!attackingColor.equals(defendingColor)) return true;
        return false;
    }


    public boolean canMove(ChessPosition startingPosition, ChessPosition finalPosition) {
        int finalRow = finalPosition.getRow();
        int finalColumn = finalPosition.getColumn();
        if (finalPosition.isInBounds() && getPiece(finalPosition) == null) return true;
        return false;
    }


    private boolean checkEqualBoard(ChessBoard board1, ChessBoard board2) {
        if (board1.toString().equals(board2.toString())) {
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        int totalHash = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                ChessPosition boardPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece currentPiece = getPiece(boardPosition);
                if (currentPiece == null) {
                    continue;
                }
                totalHash += currentPiece.hashCode();
            }
        }
        return 31 * Objects.hash(totalHash);
    }


    @Override
    public boolean equals(Object o) {
        // 1. Check for reference equality
        if (this == o) return true;
        // 2. Check for null and ensure the classes match
        if (o == null || getClass() != o.getClass()) return false;
        // 3. Cast and compare field values
        ChessBoard that = (ChessBoard) o;
        if (checkEqualBoard(this, that)) {
            return true;
        }
        return false;
    }

    private Map<ChessPiece.PieceType, String[]> pieceStringMap = Map.of(
            ChessPiece.PieceType.PAWN, new String[] {"P", "p"},
            ChessPiece.PieceType.ROOK, new String[] {"R", "r"},
            ChessPiece.PieceType.KNIGHT, new String[] {"N", "n"},
            ChessPiece.PieceType.BISHOP, new String[] {"B", "b"},
            ChessPiece.PieceType.KING, new String[] {"K", "k"},
            ChessPiece.PieceType.QUEEN, new String [] {"Q", "q"}
    );


    private String getPieceString(ChessPosition position) {
        ChessPiece piece = getPiece(position);
        if (piece == null) {
            return " ";
        }
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();
        int teamIndex;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            teamIndex = 0;
        }
        else {
            teamIndex = 1;
        }
        return pieceStringMap.get(pieceType)[teamIndex];
    }


    @Override
    public String toString() {
        String boardString = "";
        for (int i = 7; i >= 0; i--) {
            boardString += "|";
            for (int j = 0; j < board[i].length; j++) {
                ChessPosition piecePosition = new ChessPosition(i + 1, j + 1);
                String pieceString = getPieceString(piecePosition);
                boardString += pieceString + "|";
            }
            boardString += "\n";
        }
        return boardString;
    }
}
