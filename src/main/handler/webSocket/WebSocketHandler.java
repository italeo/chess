package handler.webSocket;

import chess.ChessGame;
import chess.ChessGameImpl;
import chess.ChessMove;
import chess.ChessMoveImpl;
import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.AuthToken;
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
import java.util.Objects;

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
        AuthToken auth = authTokenDAO.find(authToken);

        // Check that the authToken from the message is valid
        if (auth != null) {

            // Get the rootClient
            String rootClient = authTokenDAO.find(authToken).getUsername();

            // get the game
            Game game = gameDAO.findGameByID(gameID);

            if (gameDAO.findGameByID(gameID) != null) {

                if ((playerColor == ChessGame.TeamColor.WHITE && Objects.equals(game.getWhiteUsername(), auth.getUsername())) ||
                        (playerColor == ChessGame.TeamColor.BLACK) && Objects.equals(game.getBlackUsername(), auth.getUsername())) {

                    connections.add(gameID, session, authToken, rootClient, playerColor);

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
            } else {
                String errorMsg = String.format("game %s does not exist", gameID);
                Error error = new Error(errorMsg);
                String errorJson = gson.toJson(error);
                if (session.isOpen()) {
                    session.getRemote().sendString(errorJson);
                }
            }
        } else {
            String errorMsg = String.format("Invalid authToken: %S", authToken);
            Error error = new Error(errorMsg);
            String errorJson = gson.toJson(error);
            if (session.isOpen()) {
                session.getRemote().sendString(errorJson);
            }
        }
    }

    private void observerCmd(Session session, String message) throws DataAccessException, IOException {
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        String authToken = joinObserver.getAuthString();
        Gson gson = new Gson();
        AuthToken auth = authTokenDAO.find(authToken);
        int gameID = joinObserver.getGameID();

        if (auth != null) {
            try {
                String username = auth.getUsername();

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

    private void makeMoveCmd(Session session, String message) throws DataAccessException {
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
        String authToken = makeMove.getAuthString();
        ChessMove move = makeMove.getMove();
        Integer gameID = makeMove.getGameID();
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        AuthToken auth = authTokenDAO.find(authToken);
        String rootClient = auth.getUsername();
        Game game = gameDAO.findGameByID(gameID);
        ChessGameImpl gameImpl = new ChessGameImpl();

        // Validate the authToken and the gameID
        if (auth != null && gameDAO.findGameByID(gameID) != null) {

            // validate the move?
            gameImpl.validMoves(move.getStartPosition());

            // Update game to rep move in db

            // Send loadGame to 'ALL CLIENTS' in the game and rootClient

            //
        }


    }

    private void leaveCmd(Session session, String message) {
        Leave leave = new Gson().fromJson(message, Leave.class);
    }

    private void resignCmd(Session session, String message) {
        Resign resign = new Gson().fromJson(message, Resign.class);
    }


}
