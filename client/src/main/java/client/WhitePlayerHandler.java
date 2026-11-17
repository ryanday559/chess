package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class WhitePlayerHandler extends NotificationHandler {
    public WhitePlayerHandler() {
    }

    @Override
    protected void handleLoadGame(ServerMessage message) {
        LoadGameMessage gameMessage = (LoadGameMessage) message; //Getting an error here while casting. Can't cast from servermessage to loadgamemessage.
        ChessGame game = gameMessage.getGame();
        BoardDisplay display = new BoardDisplay(game.getBoard());
        System.out.print(display.getWhiteView());
    }
}
