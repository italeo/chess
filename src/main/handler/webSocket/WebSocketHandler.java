package handler.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSeverMessages.userCommands.*;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    // Responsible to handle all web socket connections
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // Parse the incoming JSON message
        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);

        // Handling different command types
        switch (userCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayerCmd(session, message);
            case JOIN_OBSERVER -> observerCmd(session, message);
            case MAKE_MOVE -> makeMoveCmd(session, message);
            case LEAVE -> leaveCmd(session, message);
            case RESIGN -> resignCmd(session, message);
        }
    }

    private void joinPlayerCmd(Session session, String message) {
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);

        int gameID = joinPlayer.getGameID();
        ChessGame.TeamColor playerColor = joinPlayer.getTeamColor();
        // Add the connection to the set
        connections.add(gameID, session);


    }

    private void observerCmd(Session session, String message) {
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
    }

    private void makeMoveCmd(Session session, String message) {
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
    }

    private void leaveCmd(Session session, String message) {
        Leave leave = new Gson().fromJson(message, Leave.class);
    }

    private void resignCmd(Session session, String message) {
        Resign resign = new Gson().fromJson(message, Resign.class);
    }


}
