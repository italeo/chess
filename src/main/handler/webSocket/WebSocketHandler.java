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
import webSeverMessages.serverMessages.Error;
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
        conn = db.getConnection();
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
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        String authToken = joinPlayer.getAuthString();
        Gson gson = new Gson();

        // Check that the authToken from the message is valid
        if (authTokenDAO.find(authToken) != null) {
            try {
                // The root client
                String username = authTokenDAO.find(authToken).getUsername();
                int gameID = joinPlayer.getGameID();
                ChessGame.TeamColor playerColor = joinPlayer.getTeamColor();

                // Added the connection to the map
                connections.add(gameID, session, authToken, username, playerColor);

                // Get the current state of the game
                Game currentGame = gameDAO.findGameByID(gameID);

                // Error severMessage
                String errorMsg = String.format("Sorry %s already taken", playerColor);
                Error errorNotification = new Error(errorMsg);
                String errorJson = gson.toJson(errorNotification);

                // Update the game according to player color
                if ((playerColor == ChessGame.TeamColor.WHITE && currentGame.getWhiteUsername() != null) ||
                        (playerColor == ChessGame.TeamColor.BLACK && currentGame.getBlackUsername() != null)) {
                    if (session.isOpen()) {
                        session.getRemote().sendString(errorJson);
                       return;
                    }
                }

                // Update the game according to player color
                updatePlayerColor(currentGame, playerColor, username);
                gameDAO.updateGame(currentGame);

                // Send load game back to client
                LoadGame loadGame = new LoadGame(currentGame);
                // Serialize type game to a string
                String loadGameJson = gson.toJson(loadGame);
                if (session.isOpen()) {
                    session.getRemote().sendString(loadGameJson);
                }

                // Message to notify other players
                String notificationMsg = String.format("%s joined as %s player", username, playerColor);
                Notification allOthersNotification = new Notification(notificationMsg);
                connections.broadcast(gameID, allOthersNotification, username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePlayerColor(Game currentGame, ChessGame.TeamColor playerColor, String username) {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            currentGame.setWhiteUsername(username);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            currentGame.setBlackUsername(username);
        }
    }


    private void observerCmd(Session session, String message) throws DataAccessException {
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        String authToken = joinObserver.getAuthString();

        if (authTokenDAO.find(authToken) != null) {
            try {
                String username = authTokenDAO.find(authToken).getUsername();
                int gameID = joinObserver.getGameID();

                // Add to connection set
                connections.add(gameID, session, authToken, username, null);

                // Get current state of game
                Game currentGame = gameDAO.findGameByID(gameID);

                // send loadGame back to client
                LoadGame loadGame = new LoadGame(currentGame);
                // Serialize type game to string
                String loadGameJson = new Gson().toJson(loadGame);
                // Send the serverMessage
                if (session.isOpen()) {
                    session.getRemote().sendString(loadGameJson);
                }

                // Build and send the notification to others
                String notificationMsg = String.format("%s joined as an observer.", username);
                Notification notification = new Notification(notificationMsg);
                connections.broadcast(gameID, notification, username);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }



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
