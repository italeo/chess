package handler.webSocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Connection>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session, String authToken, String username, ChessGame.TeamColor playerColor) {
        connections.compute(gameID, (key, existingSet) -> {
            if (existingSet == null) {
                existingSet = ConcurrentHashMap.newKeySet();
            }
            existingSet.add(new Connection(gameID, session, authToken, username, playerColor));
            return existingSet;
        });
    }

    public void remove(Session rootSession, Integer gameID) {
        Set<Connection> connectionSet = connections.get(gameID);
        if (connectionSet != null) {
            connectionSet.removeIf(c -> c.session.equals(rootSession));
        }
    }

    public void broadcast(Integer gameID, String notification, String excludeUsername) throws IOException {
        List<Connection> closedConnections = new ArrayList<>();
        Set<Connection> connectionSet = connections.get(gameID);

        for (var c : connectionSet) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(notification);
                }
            } else {
                closedConnections.add(c);
            }
        }

        // Clean up any open connections
        closedConnections.forEach(connectionSet::remove);
    }
}
