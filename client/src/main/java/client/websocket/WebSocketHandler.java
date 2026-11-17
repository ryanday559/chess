package client.websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements NotificationHandler {

    public WebSocketHandler() {
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame(message);
            case ERROR -> handleError(message);
            case NOTIFICATION -> handleNotification(message);
        }
    }

    private void handleLoadGame(ServerMessage message) {
        LoadGameMessage gameMessage = (LoadGameMessage) message;
    }

    private void handleError(ServerMessage message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
    }

    private void handleNotification(ServerMessage message) {
        NotificationMessage notificationMessage = (NotificationMessage) message;
    }
}
