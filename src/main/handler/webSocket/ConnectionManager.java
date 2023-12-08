package handler.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSeverMessages.serverMessages.Notification;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        connections.compute(gameID, (key, existingSet) -> {
            if (existingSet == null) {
                existingSet = ConcurrentHashMap.newKeySet();
            }
            existingSet.add(session);
            return existingSet;
        });
    }


    public void broadcast(Integer gameID, Notification notification, Session excludeSession) throws IOException {
        Set<Session> sessions = connections.get(gameID);
        System.out.println("Session for gameID " + gameID + ": " + sessions.size());
        for (var session : sessions) {
            if (session.isOpen()) {
                if (!session.equals(excludeSession)) {
                    System.out.println("Sending message to session: " + session);
                    session.getRemote().sendString(notification.toString());
                }
            }
        }
    }
}
