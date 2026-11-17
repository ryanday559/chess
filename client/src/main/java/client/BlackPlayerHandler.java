package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class BlackPlayerHandler extends NotificationHandler {
    public BlackPlayerHandler() {
    }

    @Override
    protected String handleLoadGame(ServerMessage message) {
        LoadGameMessage gameMessage = (LoadGameMessage) message;
        ChessGame game = gameMessage.getGame();
        BoardDisplay display = new BoardDisplay(game.getBoard());
        return display.getBlackView();
    }
}
