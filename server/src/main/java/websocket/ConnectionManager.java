package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
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

    public void gameBroadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcastGameUpdate(Integer gameID, LoadGameMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                c.getRemote().sendString(msg);
            }
        }
    }
}
