package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;

public class WhitePlayerHandler extends NotificationHandler {
    public WhitePlayerHandler() {
    }

    @Override
    protected void handleLoadGame(String message) {
        LoadGameMessage gameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        ChessGame game = gameMessage.getGame();
        Collection<ChessPosition> highlightMoves = gameMessage.getHighlightPositions();
        BoardDisplay display = new BoardDisplay(game.getBoard(), highlightMoves);
        System.out.print(display.getWhiteView());
    }
}
