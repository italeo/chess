package handler.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSeverMessages.serverMessages.Notification;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        connections.compute(gameID, (key, existingSet) -> {
            if (existingSet == null) {
                existingSet = new CopyOnWriteArraySet<>();
            }
            existingSet.add(session);
            return existingSet;
        });
    }

    public void remove(Integer gameID, Session session ) {
        connections.computeIfPresent(gameID, (key, existingSet) -> {
            existingSet.remove(session);
            if (existingSet.isEmpty()) {
                return null;
            }
            return existingSet;
        });
    }

    public void broadcast(Integer excludeGameID, Notification notification) throws IOException {
        for (var entry : connections.entrySet()) {
            var gameID = entry.getKey();
            var sessionSet = entry.getValue();

            if (!gameID.equals(excludeGameID)) {
                for (var session : sessionSet) {
                    if (session.isOpen()) {
                        session.getRemote().sendString(notification.getMessage());
                    }
                }
            }
        }
    }
}
