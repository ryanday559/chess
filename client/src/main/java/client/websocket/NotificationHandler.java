package client.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;

public abstract class NotificationHandler {
    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType messageType = serverMessage.getServerMessageType();
        switch (messageType) {
            case LOAD_GAME -> handleLoadGame(message);
            case ERROR -> handleError(message);
            case NOTIFICATION -> handleNotification(message);
        };
    }

    protected abstract void handleLoadGame(String message);

    private void handleError(String message) {
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
        System.out.print("\n" + errorMessage.getErrorMessage() + "\n>>> ");
    }

    private void handleNotification(String message) {
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
        System.out.print("\n" + notificationMessage.getMessage() + "\n>>> ");
    }
}