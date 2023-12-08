package handler.webSocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import webSeverMessages.serverMessages.Notification;
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



    public void broadcast(Integer gameID, Notification notification, String excludeUsername) throws IOException {
        List<Connection> closedConnections = new ArrayList<>();
        Set<Connection> connectionSet = connections.get(gameID);
        System.out.println("Session for gameID " + gameID + ": " + connectionSet.size());
        for (var c : connectionSet) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    System.out.println("user that gets notified: " + c.username);
                    System.out.println("excludedUsername: " + excludeUsername);
                    System.out.println("Sending notification to : " + c.username);
                    System.out.println("The notification: " + notification.toString());
                    c.send(notification.toString());
                }
            } else {
                closedConnections.add(c);
            }
        }
        closedConnections.forEach(connectionSet::remove);
    }
}
