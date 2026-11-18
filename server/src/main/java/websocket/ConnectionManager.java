package websocket;

import chess.ChessGame;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    // Maybe I need to update this map to be a map of maps, where the internal map stores sessions by player color and observers
    public final ConcurrentHashMap<Integer, HashSet<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        HashSet<Session> sessionSet;
        if (!connections.containsKey(gameID)) {
            sessionSet = new HashSet<Session>();
            connections.put(gameID, sessionSet);
        }
        else {
            sessionSet = connections.get(gameID);
        }
        sessionSet.add(session);
    }

    public void remove(Integer gameID, Session session) {
        HashSet<Session> sessionSet = connections.get(gameID);
        sessionSet.remove(session);
    }

    public void selfBroadcastGameUpdate(LoadGameMessage message, Session session) throws IOException {
        String msg = message.toString();
        session.getRemote().sendString(msg);
    }

    public void selfBroadcastErrorMessage(ErrorMessage message, Session session) throws IOException {
        String msg = message.toString();
        session.getRemote().sendString(msg);
    }

    public void broadcastGameUpdate(Integer gameID, LoadGameMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                c.getRemote().sendString(msg);
            }
        }
    }

    public void broadcastJoinMessage(GameData gameData, String username) throws IOException {
        String whitePlayer = gameData.whiteUsername();
        String blackPlayer = gameData.blackUsername();
        String colorMessage;
        if (whitePlayer != null && whitePlayer.equals(username)) {
            colorMessage = " as the white player!";
        }
        else if (blackPlayer != null && blackPlayer.equals(username)) {
            colorMessage = " as the black player!";
        }
        else {
            colorMessage = " as an observer!";
        }
        NotificationMessage notificationMessage = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                username + " joined" + colorMessage
            );
        broadcastMessage(gameData.gameID(), notificationMessage);
    }

    public void broadcastMessage(Integer gameID, NotificationMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                c.getRemote().sendString(msg);
            }
        }
    }
}
