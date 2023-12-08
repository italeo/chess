package handler.webSocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public Integer gameID;
    public Session session;
    public String authToken;
    public String username;
    public ChessGame.TeamColor playerColor;

    public Connection(Integer gameID, Session session, String authToken, String username, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.session = session;
        this.authToken = authToken;
        this.username = username;
        this.playerColor = playerColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    @Override
    public int hashCode() {
        return authToken.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return authToken.equals(((Connection) obj).authToken);
    }
}
