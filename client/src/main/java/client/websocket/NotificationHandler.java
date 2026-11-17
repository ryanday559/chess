package client.websocket;

import chess.ChessGame;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public abstract class NotificationHandler {
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame(message);
            case ERROR -> handleError(message);
            case NOTIFICATION -> handleNotification(message);
        };
    }

    protected abstract void handleLoadGame(ServerMessage message);

    private void handleError(ServerMessage message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        System.out.print(errorMessage.getErrorMessage());
    }

    private void handleNotification(ServerMessage message) {
        NotificationMessage notificationMessage = (NotificationMessage) message;
        System.out.print(notificationMessage.getMessage());
    }
}