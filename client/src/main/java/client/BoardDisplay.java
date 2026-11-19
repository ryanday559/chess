package client;

import chess.*;

import java.util.Collection;

import static ui.EscapeSequences.*;

public class BoardDisplay {
    private ChessBoard board;
    private Collection<ChessPosition> highlightPositions;
    private final String boardLettering = "   a  b  c  d  e  f  g  h   ";
    private final String reversedBoardLettering = "   h  g  f  e  d  c  b  a   ";

    public BoardDisplay(ChessBoard board, Collection<ChessPosition> highlightPositions) {
        this.board = board;
        this.highlightPositions = highlightPositions;
    }

    public String getWhiteView() {
        String boardString = "\n" + SET_BG_COLOR_LIGHT_GREY + " " + boardLettering + " " + RESET_BG_COLOR + "\n";
        for (int i = 8; i >= 1; i--) {
            if (i % 2 == 0) {
                boardString += prepareWhiteFirstSpaceRow(i);
            }
            else {
                boardString += prepareBlackFirstSpaceRow(i);
            }
        }
        boardString += SET_BG_COLOR_LIGHT_GREY + " " + boardLettering + " " + RESET_BG_COLOR + "\n>>> ";
        return boardString;
    }

    private String prepareWhiteFirstSpaceRow(int row) {
        String rowString = SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR;
        for (int i = 1; i < 9; i++) {
            rowString += setCellWhiteColumnOdd(row, i);
        }
        return rowString + SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR + "\n";
    }

    private String prepareReverseWhiteFirstSpaceRow(int row) {
        String rowString = SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR;
        for (int i = 8; i >= 1; i--) {
            rowString += setCellWhiteColumnEven(row, i);
        }
        return rowString + SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR + "\n";
    }

    private String getWhiteCellBackgroundColor(int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        if (highlightPositions.contains(position)) {
            return SET_BG_COLOR_PALE_YELLOW;
        }
        return SET_BG_COLOR_WHITE;
    }

    private String getBlackCellBackgroundColor(int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        if (highlightPositions.contains(position)) {
            return SET_BG_COLOR_DARK_YELLOW;
        }
        return SET_BG_COLOR_BLACK;
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
            rowString += setCellWhiteColumnEven(row, i);
        }
        return rowString + SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR + "\n";
    }

    private String setCellWhiteColumnEven(int row, int col) {
        String pieceString = getPieceWithColor(row, col);
        if (col % 2 == 0) {
            String cellColor = getWhiteCellBackgroundColor(row, col);
            return cellColor + " " + pieceString + " " + RESET_BG_COLOR;
        }
        else {
            String cellColor = getBlackCellBackgroundColor(row, col);
            return cellColor + " " + pieceString + " " + RESET_BG_COLOR;
        }
    }

    private String prepareReverseBlackFirstSpaceRow(int row) {
        String rowString = SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR;
        for (int i = 8; i >= 1; i--) {
            rowString += setCellWhiteColumnOdd(row, i);
        }
        return rowString + SET_BG_COLOR_LIGHT_GREY + " " + String.valueOf(row) + " " + RESET_BG_COLOR + "\n";
    }

    private String setCellWhiteColumnOdd(int row, int col) {
        String pieceString = getPieceWithColor(row, col);
        if (col % 2 == 1) {
            String cellColor = getWhiteCellBackgroundColor(row, col);
            return cellColor + " " + pieceString + " " + RESET_BG_COLOR;
        }
        else {
            String cellColor = getBlackCellBackgroundColor(row, col);
            return cellColor + " " + pieceString + " " + RESET_BG_COLOR;
        }
    }

    public String getBlackView() {
        String boardString = "\n" + SET_BG_COLOR_LIGHT_GREY + " " + reversedBoardLettering + " " + RESET_BG_COLOR + "\n";
        for (int i = 1; i <= 8; i++) {
            if (i % 2 == 1) {
                boardString += prepareReverseWhiteFirstSpaceRow(i);
            }
            else {
                boardString += prepareReverseBlackFirstSpaceRow(i);
            }
        }
        boardString += SET_BG_COLOR_LIGHT_GREY + " " + reversedBoardLettering + " " + RESET_BG_COLOR + "\n>>> ";
        return boardString;
    }
}
