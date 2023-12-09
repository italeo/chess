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
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
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

    private void joinPlayerCmd(Session session, String message) throws DataAccessException, IOException {
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        String authToken = joinPlayer.getAuthString();
        Integer gameID = joinPlayer.getGameID();
        ChessGame.TeamColor playerColor = joinPlayer.getTeamColor();
        Gson gson = new Gson();

        // Check that the authToken from the message is valid
        if (authTokenDAO.find(authToken) != null) {

            // Get the rootClient
            String rootClient = authTokenDAO.find(authToken).getUsername();

            // get the game
            Game game = gameDAO.findGameByID(gameID);

            if ((playerColor == ChessGame.TeamColor.WHITE && game.getWhiteUsername() == null) ||
                    (playerColor == ChessGame.TeamColor.BLACK) && game.getBlackUsername() == null) {

                connections.add(gameID, session, authToken, rootClient, playerColor);

                updatePlayerColor(game, playerColor, rootClient);
                gameDAO.updateGame(game);

                // create LoadGame
                LoadGame loadGame = new LoadGame(game);
                String loadGameJson = gson.toJson(loadGame);
                if (session.isOpen()) {
                    session.getRemote().sendString(loadGameJson);
                }

                // Send Notification to others
                String notificationMsg = String.format("%s joined as %s player", rootClient, playerColor);
                Notification notification = new Notification(notificationMsg);
                String notificationJson = gson.toJson(notification);
                connections.broadcast(gameID, notificationJson, rootClient);
            } else {
                String errorMsg = String.format("%s is already taken", playerColor);
                Error error = new Error(errorMsg);
                String errorJson = gson.toJson(error);
                if (session.isOpen()) {
                    session.getRemote().sendString(errorJson);
                }
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


    private void observerCmd(Session session, String message) throws DataAccessException, IOException {
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        String authToken = joinObserver.getAuthString();
        Gson gson = new Gson();

        if (authTokenDAO.find(authToken) != null) {
            try {
                String username = authTokenDAO.find(authToken).getUsername();
                int gameID = joinObserver.getGameID();

                if (gameDAO.findGameByID(gameID) != null) {
                    // Add to connection set
                    connections.add(gameID, session, authToken, username, null);

                    // Get current state of game
                    Game currentGame = gameDAO.findGameByID(gameID);

                    // send loadGame back to client
                    LoadGame loadGame = new LoadGame(currentGame);
                    // Serialize type game to string
                    String loadGameJson = gson.toJson(loadGame);
                    // Send the serverMessage
                    if (session.isOpen()) {
                        session.getRemote().sendString(loadGameJson);
                    }

                    // Build and send the notification to others
                    String notificationMsg = String.format("%s joined as an observer.", username);
                    Notification notification = new Notification(notificationMsg);
                    String notificationJson = gson.toJson(notification);
                    connections.broadcast(gameID, notificationJson, username);
                } else {
                    String errorMSg = String.format("Sorry the gameID: %s, is incorrect", gameID);
                    Error error = new Error(errorMSg);
                    String errorJson = gson.toJson(error);
                    if (session.isOpen()) {
                        session.getRemote().sendString(errorJson);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String errorMSg = String.format("Sorry the authToken: %s, is incorrect", authToken);
            Error error = new Error(errorMSg);
            String errorJson = gson.toJson(error);
            if (session.isOpen()) {
                session.getRemote().sendString(errorJson);
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
