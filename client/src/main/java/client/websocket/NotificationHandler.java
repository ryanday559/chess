package client.websocket;

import chess.ChessGame;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public abstract class NotificationHandler {
    public String notify(ServerMessage message) {
        return switch (message.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame(message);
            case ERROR -> handleError(message);
            case NOTIFICATION -> handleNotification(message);
        };
    }

    protected abstract String handleLoadGame(ServerMessage message);

    private String handleError(ServerMessage message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        return errorMessage.getErrorMessage();
    }

    private String handleNotification(ServerMessage message) {
        NotificationMessage notificationMessage = (NotificationMessage) message;
        return notificationMessage.getMessage();
    }
}