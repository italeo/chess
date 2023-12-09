package handler.webSocket;

import chess.*;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    // Responsible to handle all web socket connections
    private final ConnectionManager connections = new ConnectionManager();
    private final Database db = new Database();
    private Connection conn;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
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

    private void makeMoveCmd(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
        String authToken = makeMove.getAuthString();
        ChessMove move = makeMove.getMove();
        Integer gameID = makeMove.getGameID();
        GameDAO gameDAO = new GameDAO(conn);
        Gson gson = new Gson();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        AuthToken auth = authTokenDAO.find(authToken);
        String rootClient = auth.getUsername();
        Game game = gameDAO.findGameByID(gameID);

        // Validate the authToken and the gameID
        if (gameDAO.findGameByID(gameID) != null) {
            System.out.println("gameID is valid");
            // validate the move?
            if(isValidMove(move, game)) {

                // Make the move/update the move on the board for the game
                game.getGame().makeMove(move);
                // Update that move in the db
                gameDAO.updateGame(game);

                // Create the LoadGame server message
                LoadGame loadGame = new LoadGame(game);
                String loadGameJson = gson.toJson(loadGame);
                if (session.isOpen()) {
                    session.getRemote().sendString(loadGameJson);
                }

                // Build the Notification server message

                // The piece in action
                ChessPiece piece = game.getGame().getBoard().getPiece(move.getStartPosition());

                // The position the piece was moved to
                ChessPosition position = move.getEndPosition();
                String notificationMsg = String.format("%s moved to %s", piece, position);
                Notification notification = new Notification(notificationMsg);
                String notificationJson = gson.toJson(notification);
                connections.broadcast(gameID, notificationJson, rootClient);
            } else {
                Error errorMsg = new Error("Invalid move");
                if (session.isOpen()) {
                    session.getRemote().sendString(gson.toJson(errorMsg));
                }
            }
        } else {
            Error errorMsg = new Error("authToken or gameID is invalid");
            if (session.isOpen()) {
                session.getRemote().sendString(gson.toJson(errorMsg));
            }
        }
    }

    private boolean isValidMove(ChessMove move, Game game) {
        Collection<ChessMove> validMoves = game.getGame().validMoves(move.getStartPosition());
        if (!validMoves.isEmpty() && validMoves.contains(move)) {
            return true;
        } else {
            return false;
        }
    }

    private void leaveCmd(Session session, String message) throws DataAccessException, IOException {
        Leave leave = new Gson().fromJson(message, Leave.class);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        GameDAO gameDAO = new GameDAO(conn);
        Game game = gameDAO.findGameByID(leave.getGameID());
        int gameID = leave.getGameID();
        AuthToken auth = authTokenDAO.find(leave.getAuthString());
        Gson gson = new Gson();

        // Validate the game and the authToken is the correct one
        if (game != null && auth != null) {
            String rootClient = auth.getUsername();

            // Remove the root client
            connections.remove(session, rootClient, gameID);

            // update the changes in the game
            gameDAO.updateGame(game);

            // build the notification for all other users/client
            String notificationMsg = String.format("%s has left the game", rootClient);
            Notification notification = new Notification(notificationMsg);
            String notificationJson = gson.toJson(notification);
            connections.broadcast(gameID, notificationJson, "");
        } else {
            // Build the Error server message
            Error error = new Error("Error leaving the game");
            String errorJson = gson.toJson(error);
            session.getRemote().sendString(errorJson);
        }
    }

    private void resignCmd(Session session, String message) {
        Resign resign = new Gson().fromJson(message, Resign.class);
    }


}
