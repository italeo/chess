package handler.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSeverMessages.serverMessages.LoadGame;
import webSeverMessages.serverMessages.Notification;
import webSeverMessages.userCommands.*;
import webSocketMessages.userCommands.UserGameCommand;
import java.sql.Connection;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    // Responsible to handle all web socket connections
    private final ConnectionManager connections = new ConnectionManager();
    private final Database db = new Database();
    private Connection conn;
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
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

    private void joinPlayerCmd(Session session, String message) throws IOException, DataAccessException {
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        conn = db.getConnection();
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        joinPlayer.getAuthString();

        // Check that the authToken from the message is valid
        System.out.print(message);

        int gameID = joinPlayer.getGameID();
        ChessGame.TeamColor playerColor = joinPlayer.getTeamColor();
        // Add the connection to the set
        connections.add(gameID, session);

        // Get the current state of the game
        var currentGameState = gameDAO.findGameByID(gameID);

        // Send load game back to client
        LoadGame loadGame = new LoadGame(currentGameState);
        var rootMessage = String.format("Game loaded");
        Notification rootClientNotification = new Notification(rootMessage);
        System.out.print(rootMessage);
        session.getRemote().sendString(rootClientNotification.getMessage());


        // Message to notify other players
        String msg = String.format("%s joined as %s player", message, playerColor);
        var allOthersNotification = new Notification(msg);
        System.out.print(msg);
        connections.broadcast(gameID, allOthersNotification);
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
