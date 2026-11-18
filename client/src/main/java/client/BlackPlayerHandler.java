package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import com.google.gson.Gson;
import jakarta.websocket.Endpoint;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;

public class BlackPlayerHandler extends NotificationHandler {
    public BlackPlayerHandler() {
    }

    @Override
    protected void handleLoadGame(String message) {
        LoadGameMessage gameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        ChessGame game = gameMessage.getGame();
        Collection<ChessPosition> moves = gameMessage.getHighlightPositions();
        BoardDisplay display = new BoardDisplay(game.getBoard(), moves);
        System.out.print(display.getBlackView());
    }
}
