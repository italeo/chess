package handler.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
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

        String notificationJson = new Gson().toJson(notification);
        for (var c : connectionSet) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(notificationJson);
                    System.out.println("In the Notifications message");
                }
            } else {
                closedConnections.add(c);
            }
        }
        closedConnections.forEach(connectionSet::remove);
    }
}
