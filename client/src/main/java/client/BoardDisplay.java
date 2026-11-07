package client;

import chess.ChessBoard;
import chess.ChessGame;

import static ui.EscapeSequences.*;

public class BoardDisplay {
    private ChessBoard board;
    private final String boardLettering = " abcdefgh ";
    private final String reversedBoardLettering = " hgfedcba ";

    public BoardDisplay(ChessBoard board) {
        this.board = board;
    }

    public void printWhiteView() {
        System.out.printf(SET_BG_COLOR_LIGHT_GREY + boardLettering + RESET_BG_COLOR + "\n");
        for (int i = 1; i <=8; i++) {
        }
    }

    public void printBlackView() {
        System.out.printf(SET_BG_COLOR_LIGHT_GREY + reversedBoardLettering + RESET_BG_COLOR);
    }
}
