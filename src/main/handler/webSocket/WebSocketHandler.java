package handler.webSocket;

import chess.*;
import com.google.gson.*;
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
import java.lang.reflect.Type;
import java.sql.Connection;
import java.io.IOException;
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

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessMove.class, new ChessMoveDeserializer());
        builder.registerTypeAdapter(ChessPosition.class, new ChessPositionDeserializer());

        MakeMove makeMove = builder.create().fromJson(message, MakeMove.class);
        String authToken = makeMove.getAuthString();
        ChessMove move = makeMove.getMove();
        Integer gameID = makeMove.getGameID();
        GameDAO gameDAO = new GameDAO(conn);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        Gson gson = new Gson();
        AuthToken auth = authTokenDAO.find(authToken);
        String rootClient = auth.getUsername();
        Game game = gameDAO.findGameByID(gameID);

        System.out.println("The game: " + game);

        // Validate the authToken and the gameID
        if (game != null) {
            System.out.println("gameID is valid");

            // Check if it's the users' turn
            if (isPlayersTurn(auth, game)) {


                // validate the move?
                if (isValidMove(move, game)) {

                    System.out.println("After the isValidMove check");
                    // Make the move/update the move on the board for the game
                    System.out.println("the game: " + game);
                    game.getGame().makeMove(move);
                    // Update that move in the db
                    gameDAO.updateGame(game);
                    System.out.println("After the updateGame");

                    // Create the LoadGame server message
                    LoadGame loadGame = new LoadGame(game);
                    String loadGameJson = gson.toJson(loadGame);
                    if (session.isOpen()) {
                        session.getRemote().sendString(loadGameJson);
                        System.out.println("After sending LoadGame");
                    }
                    connections.broadcast(gameID, loadGameJson, rootClient);

                    // Build the Notification server message

                    // The piece in action
                    ChessPiece piece = game.getGame().getBoard().getPiece(move.getStartPosition());

                    // The position the piece was moved to
                    ChessPosition position = move.getEndPosition();
                    String notificationMsg = String.format("%s moved to %s", piece, position);
                    Notification notification = new Notification(notificationMsg);
                    String notificationJson = gson.toJson(notification);
                    connections.broadcast(gameID, notificationJson, rootClient);
                    System.out.println("After send th notification");

                } else {
                    Error errorMsg = new Error("Invalid move");
                    if (session.isOpen()) {
                        session.getRemote().sendString(gson.toJson(errorMsg));
                        System.out.println("In isValidMove error");
                    }
                }
            } else {
                Error errorMsg = new Error("Sorry not your turn");
                if (session.isOpen()) {
                    session.getRemote().sendString(gson.toJson(errorMsg));
                    System.out.println("In playerTurn error");
                }
            }
        } else {
            Error errorMsg = new Error("authToken or gameID is invalid");
            if (session.isOpen()) {
                session.getRemote().sendString(gson.toJson(errorMsg));
                System.out.println("In gameID null error");
            }
        }
    }

    private boolean isPlayersTurn(AuthToken auth, Game game) {
        // Get the chess game
        ChessGame chessGame = game.getGame();
        // Get whose turn it is
        ChessGame.TeamColor teamColor = chessGame.getTeamTurn();
        return (auth.getUsername().equals(game.getWhiteUsername()) && teamColor == ChessGame.TeamColor.WHITE) ||
                (auth.getUsername().equals(game.getBlackUsername()) && teamColor == ChessGame.TeamColor.BLACK);
    }

    private boolean isValidMove(ChessMove move, Game game) {
        ChessGame chessGame = game.getGame();
        System.out.println("chessGame: " + chessGame);
        Collection<ChessMove> validMoves = chessGame.validMoves(move.getStartPosition());
        return !validMoves.isEmpty() && validMoves.contains(move);

//        return false;
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

    private static class ChessMoveDeserializer implements JsonDeserializer<ChessMove> {

        @Override
        public ChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return ctx.deserialize(el, ChessMoveImpl.class);
        }
    }

    private static class ChessPositionDeserializer implements JsonDeserializer<ChessPosition> {

        @Override
        public ChessPosition deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return ctx.deserialize(el, ChessPositionImpl.class);
        }
    }
}
