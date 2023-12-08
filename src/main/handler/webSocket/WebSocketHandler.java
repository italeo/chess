package handler.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.Game;
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
    public void onMessage(Session session, String message) throws DataAccessException {
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

    private void joinPlayerCmd(Session session, String message) throws DataAccessException {
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        conn = db.getConnection();
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        String authToken = joinPlayer.getAuthString();

        // Check that the authToken from the message is valid
        if (authTokenDAO.find(authToken) != null) {
            String username = authTokenDAO.find(authToken).getUsername();
            try {
                int gameID = joinPlayer.getGameID();
                ChessGame.TeamColor playerColor = joinPlayer.getTeamColor();

                // Added the connection to the map
                connections.add(gameID, session);

                // Get the current state of the game
                Game currentGame = gameDAO.findGameByID(gameID);
                System.out.println(currentGame);
                // Send load game back to client
                LoadGame loadGame = new LoadGame(currentGame);
                System.out.println(loadGame);
                // Serialize type game to a string
                String loadGameJson = new Gson().toJson(loadGame);
                if (session.isOpen()) {
                    session.getRemote().sendString(loadGameJson);
                    System.out.println(loadGameJson);
                }

                // Message to notify other players
                String msg = String.format("%s joined as %s player", username, playerColor);
                System.out.println(msg);
                var allOthersNotification = new Notification(msg);
                System.out.println(allOthersNotification);
                connections.broadcast(gameID, allOthersNotification, session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
