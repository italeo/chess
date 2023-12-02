package handler.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class Connection {
    public Integer gameIndex;
    public Session session;

    public Connection(int gameIndex, Session session) {
        this.gameIndex = gameIndex;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
