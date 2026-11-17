package client;

import chess.ChessBoard;
import chess.ChessGame;

public class GameREPL {
    public enum UserStatus {
        OBSERVER,
        WHITEPLAYER,
        BLACKPLAYER
    }
    private BoardDisplay boardDisplay = new BoardDisplay(new ChessBoard());

    public GameREPL(UserStatus userStatus) {
        switch (userStatus) {
            case WHITEPLAYER -> whitePlayerREPL();
            case BLACKPLAYER -> blackPlayerREPL();
            case OBSERVER -> observerREPL();
        }
    }

    private void observerREPL() {

    }

    private void whitePlayerREPL() {

    }

    private void blackPlayerREPL() {
    }

}
