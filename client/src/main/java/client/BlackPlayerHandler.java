package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import com.google.gson.Gson;
import jakarta.websocket.Endpoint;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class BlackPlayerHandler extends NotificationHandler {
    public BlackPlayerHandler() {
    }

    @Override
    protected void handleLoadGame(String message) {
        LoadGameMessage gameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        ChessGame game = gameMessage.getGame();
        BoardDisplay display = new BoardDisplay(game.getBoard());
        System.out.print(display.getBlackView());
    }
}
