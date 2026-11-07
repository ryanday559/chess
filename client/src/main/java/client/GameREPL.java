package client;

import chess.ChessBoard;
import chess.ChessGame;

public class GameREPL {
    public enum UserStatus {
        OBSERVER,
        PLAYER
    }
    private BoardDisplay boardDisplay = new BoardDisplay(new ChessBoard());

    public GameREPL(UserStatus userStatus) {
        if (userStatus == UserStatus.OBSERVER) {
            observerREPL();
        }
        else {
            playerREPL();
        }
    }

    private void observerREPL() {
        boardDisplay.printWhiteView();
    }

    private void playerREPL() {
        boardDisplay.printWhiteView();
    }

}
