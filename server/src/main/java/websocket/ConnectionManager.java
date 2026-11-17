package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
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
        connections.remove(gameID, session);
    }

    public void gameBroadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = ServerMessage.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
