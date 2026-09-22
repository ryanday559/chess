package chess;

import java.util.List;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board = new ChessPiece[8][8];


    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        board[row - 1][col - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return board[row - 1][col - 1];
    }


    private void setPawnRow(int row, ChessGame.TeamColor color) {
        for (int i = 0; i < board[row - 1].length; i++) {
            ChessPosition position = new ChessPosition(row, i + 1);
            ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            addPiece(position, piece);
        }
    }


    private void setSpecialRow(int row, ChessGame.TeamColor color) {
        chess.ChessPiece.PieceType[] rowPieceTypes = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        for (int i = 0; i < rowPieceTypes.length; i++) {
            ChessPosition position = new ChessPosition(row, i + 1);
            ChessPiece piece = new ChessPiece(color, rowPieceTypes[i]);
            addPiece(position, piece);
        }
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        setSpecialRow(1, ChessGame.TeamColor.WHITE);
        setPawnRow(2, ChessGame.TeamColor.WHITE);
        setPawnRow(7, ChessGame.TeamColor.BLACK);
        setSpecialRow(8, ChessGame.TeamColor.BLACK);
    }


    public boolean moveOutOfBounds(ChessMove move) {
        ChessPosition endPosition = move.getEndPosition();
        if (endPosition.getRow() > board.length ||
            endPosition.getRow() < 1 ||
            endPosition.getColumn() > board[0].length ||
            endPosition.getColumn() < 1
        ) {
            return true;
        }
        return false;
    }


    public boolean canCapture(ChessPosition startPosition, ChessPosition endPosition) {
        if (getPiece(endPosition) == null) {
            return false;
        }
        ChessGame.TeamColor attackingColor = getPiece(startPosition).getTeamColor();
        ChessGame.TeamColor defendingColor = getPiece(endPosition).getTeamColor();
        if (attackingColor == defendingColor) {
            return false;
        }
        return true;
    }


    public boolean isValidMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        if (moveOutOfBounds(move)) {
            return false;
        }
        else if (
            getPiece(endPosition) == null ||
            canCapture(startPosition, endPosition)
        ) {
            return true;
        }
        return false;
    }


    private boolean checkEqualBoard(ChessBoard otherBoard) {
        if (otherBoard.toString().equals(toString())) {
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return checkEqualBoard(that);
    }


    @Override
    public int hashCode() {
        int totalHash = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = board[i].length; j > 0; j--) {
                ChessPosition position = new ChessPosition(i + 1, j);
                ChessPiece piece = getPiece(position);
                if (piece == null) {
                    continue;
                }
                totalHash += Objects.hash(piece);
            }
        }
        return totalHash * 31;
    }


    @Override
    public String toString() {
        String boardString = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = board[i].length; j > 0; j--) {
                ChessPosition position = new ChessPosition(i + 1, j);
                ChessPiece piece = getPiece(position);
                if (piece == null) {
                    boardString += "|   |";
                }
                else {
                    boardString += "| " + piece.toString() + " |";
                }
            }
            boardString += "\n";
        }
        return boardString;
    }
}
