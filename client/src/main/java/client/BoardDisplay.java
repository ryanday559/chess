package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardDisplay {
    private ChessBoard board;
    private final String boardLettering = "   a  b  c  d  e  f  g  h   ";
    private final String reversedBoardLettering = "   h  g  f  e  d  c  b  a   ";

    public BoardDisplay(ChessBoard board) {
        this.board = board;
        this.board.resetBoard();
    }

    public void printWhiteView() {
        String boardString = "\n" + SET_BG_COLOR_LIGHT_GREY + " " + boardLettering + " " + RESET_BG_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            if (i % 2 == 0) {
                boardString += prepareWhiteFirstSpaceRow(i);
            }
            else {
                boardString += prepareBlackFirstSpaceRow(i);
            }
        }
        boardString += SET_BG_COLOR_LIGHT_GREY + " " + boardLettering + " " + RESET_BG_COLOR + "\n";
        System.out.print(boardString);
    }

    private String prepareWhiteFirstSpaceRow(int row) {
        String rowString = SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR;
        for (int i = 1; i < 9; i++) {
            String pieceString = getPieceWithColor(row, i);
            if (i % 2 == 1) {
                rowString += SET_BG_COLOR_WHITE + " " + pieceString + " " + RESET_BG_COLOR;
            }
            else {
                rowString += SET_BG_COLOR_BLACK + " " + pieceString + " " + RESET_BG_COLOR;
            }
        }
        return rowString + SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR + "\n";
    }

    String getPieceWithColor(int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        String pieceString;
        if (piece == null) {
            return " ";
        }
        pieceString = piece.toString();
        String colorCode = switch(piece.getTeamColor()) {
            case BLACK -> SET_TEXT_COLOR_BLUE;
            case WHITE -> SET_TEXT_COLOR_RED;
        };
        return colorCode + pieceString + RESET_TEXT_COLOR;
    }

    private String prepareBlackFirstSpaceRow(int row) {
        String rowString = SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR;
        for (int i = 1; i < 9; i++) {
            String pieceString = getPieceWithColor(row, i);
            if (i % 2 == 0) {
                rowString += SET_BG_COLOR_WHITE + " " + pieceString + " " + RESET_BG_COLOR;
            }
            else {
                rowString += SET_BG_COLOR_BLACK + " " + pieceString + " " + RESET_BG_COLOR;
            }
        }
        return rowString + SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR + "\n";
    }

    public void printBlackView() {
        String boardString = "\n" + SET_BG_COLOR_LIGHT_GREY + " " + reversedBoardLettering + " " + RESET_BG_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            if (i % 2 == 0) {
                boardString += prepareWhiteFirstSpaceRow(i);
            }
            else {
                boardString += prepareBlackFirstSpaceRow(i);
            }
        }
        boardString += SET_BG_COLOR_LIGHT_GREY + " " + reversedBoardLettering + " " + RESET_BG_COLOR + "\n";
        System.out.print(boardString);
    }
}
