package handler.webSocket;

import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    public Integer gameID;
    public Session session;

    public Connection(Integer gameID, Session session) {
        this.gameID = gameID;
        this.session = session;
    }
}
