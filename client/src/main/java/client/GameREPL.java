package client;

import chess.ChessBoard;
import chess.ChessGame;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import service.UserService;

public class GameREPL {
    public enum UserStatus {
        OBSERVER,
        WHITEPLAYER,
        BLACKPLAYER
    }
    WebSocketFacade webSocketFacade;
    String url;

    public GameREPL(UserStatus userStatus, String url) {
        this.url = url;
        switch (userStatus) {
            case WHITEPLAYER -> whitePlayerREPL();
            case BLACKPLAYER -> blackPlayerREPL();
            case OBSERVER -> observerREPL();
        }
    }

    private void observerREPL() {
        try {
            webSocketFacade = new WebSocketFacade(url, new WhitePlayerHandler());
        } catch (ResponseException ex) {
            System.out.println("Error: Cannot Connect to Server!");
        }
    }

    private void whitePlayerREPL() {
        try {
            webSocketFacade = new WebSocketFacade(url, new WhitePlayerHandler());
        } catch (ResponseException ex) {
            System.out.println("Error: Cannot Connect to Server!");
        }
    }

    private void blackPlayerREPL() {
        try {
            webSocketFacade = new WebSocketFacade(url, new BlackPlayerHandler());
        } catch (ResponseException ex) {
            System.out.println("Error: Cannot Connect to Server!");
        }
    }

}
