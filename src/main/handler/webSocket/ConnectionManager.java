package handler.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(Integer gameIndex, Session session) {
        var connection = new Connection(gameIndex, session);
        connections.put(gameIndex, connection);
    }

    public void remove(Integer gameIndex) { connections.remove(gameIndex); }

    public void broadcast(Integer excludeGameIndex, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.gameIndex.equals(excludeGameIndex)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Removing any connections that were left open
        for (var c : removeList) {
            connections.remove(c.gameIndex);
        }

    }
}
