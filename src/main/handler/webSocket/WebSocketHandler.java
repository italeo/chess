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

                    game.getGame().setTeamTurn(playerColor);

                    // create LoadGame
                    LoadGame loadGame = new LoadGame(game);
                    String loadGameJson = gson.toJson(loadGame);
                    if (session.isOpen()) {
                        session.getRemote().sendString(loadGameJson);
                    }

                    // Send Notification to others
                    String notificationMsg = String.format("%s joined as %s player\n", rootClient, playerColor);
                    Notification notification = new Notification(notificationMsg);
                    String notificationJson = gson.toJson(notification);
                    connections.broadcast(gameID, notificationJson, rootClient);
                } else {
                    String errorMsg = String.format("%s is already taken\n", playerColor);
                    Error error = new Error(errorMsg);
                    String errorJson = gson.toJson(error);
                    if (session.isOpen()) {
                        session.getRemote().sendString(errorJson);
                    }
                }
            } else {
                String errorMsg = String.format("game %s does not exist\n", gameID);
                Error error = new Error(errorMsg);
                String errorJson = gson.toJson(error);
                if (session.isOpen()) {
                    session.getRemote().sendString(errorJson);
                }
            }
        } else {
            String errorMsg = String.format("Invalid authToken: %s\n", authToken);
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
                    String notificationMsg = String.format("%s joined as an observer.\n", username);
                    Notification notification = new Notification(notificationMsg);
                    String notificationJson = gson.toJson(notification);

                    // Broadcast loadGame to everyone else
                    connections.broadcast(gameID, notificationJson, username);

                } else {
                    String errorMSg = String.format("Sorry the gameID: %s, is incorrect\n", gameID);
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
            String errorMSg = String.format("Sorry the authToken: %s, is incorrect\n", authToken);
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
        Game game = gameDAO.findGameByID(gameID);

        // Validate the authToken and the gameID
        if (game != null && !game.getGame().isMarkEndOfGame()) {

            // Check if it's the users' turn
            if (isPlayersTurn(auth, game)) {

                String rootClient = auth.getUsername();
                // validate the move?
                if (isValidMove(move, game)) {

                    // Make the move/update the move on the board for the game
                    game.getGame().makeMove(move);
                    // Update that move in the db
                    gameDAO.updateGame(game);

                    // Create the LoadGame server message
                    LoadGame loadGame = new LoadGame(game);
                    String loadGameJson = gson.toJson(loadGame);
                    connections.broadcast(gameID, loadGameJson, rootClient);
                    if (session.isOpen()) {
                        session.getRemote().sendString(loadGameJson);
                    }

                    String notificationMsg = "move was made\n";
                    Notification notification = new Notification(notificationMsg);
                    String notificationJson = gson.toJson(notification);
                    connections.broadcast(gameID, notificationJson, rootClient);

                } else {
                    Error errorMsg = new Error("Invalid move\n");
                    if (session.isOpen()) {
                        session.getRemote().sendString(gson.toJson(errorMsg));
                    }
                }
            } else {
                Error errorMsg = new Error("Sorry not your turn\n");
                if (session.isOpen()) {
                    session.getRemote().sendString(gson.toJson(errorMsg));
                }
            }
        } else {
            Error errorMsg = new Error("authToken or gameID is invalid\n");
            if (session.isOpen()) {
                session.getRemote().sendString(gson.toJson(errorMsg));
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
        Collection<ChessMove> validMoves = chessGame.validMoves(move.getStartPosition());
        return !validMoves.isEmpty() && validMoves.contains(move);
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

            // Making sure we switch that the user has left the game by setting the teamTurn to null
            if (Objects.equals(rootClient, game.getBlackUsername())) {
                game.setBlackUsername(null);
            } else if (Objects.equals(rootClient, game.getWhiteUsername())) {
                game.setWhiteUsername(null);
            }

            // update the changes in the game
            gameDAO.updateGame(game);

            // Remove the root client
            connections.remove(session, gameID);


            // build the notification for all other users/client
            String notificationMsg = String.format("%s has left the game\n", rootClient);
            Notification notification = new Notification(notificationMsg);
            String notificationJson = gson.toJson(notification);
            connections.broadcast(gameID, notificationJson, "");
        } else {
            // Build the Error server message
            Error error = new Error("Error leaving the game\n");
            String errorJson = gson.toJson(error);
            session.getRemote().sendString(errorJson);
        }
    }

    private void resignCmd(Session session, String message) throws DataAccessException, IOException {
        Resign resign = new Gson().fromJson(message, Resign.class);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        GameDAO gameDAO = new GameDAO(conn);
        int gameID = resign.getGameID();
        Game game = gameDAO.findGameByID(gameID);
        AuthToken auth = authTokenDAO.find(resign.getAuthString());
        Gson gson = new Gson();
        String rootClient = auth.getUsername();

        if (game != null && !game.getGame().isMarkEndOfGame() && (game.getBlackUsername().equals(rootClient) ||
                game.getWhiteUsername().equals(rootClient))) {

            // Set the end of game status
            game.getGame().setMarkEndOfGame(true);
            // Update the new changes in the db
            gameDAO.updateGame(game);

            // Build the Notification serverMessage, which will be sent to all clients in the game
            String notificationMsg = String.format("\n%s has resigned the game. And therefore forfeited the game!!\n", rootClient);
            Notification notification = new Notification(notificationMsg);
            String notificationJson = gson.toJson(notification);
            connections.broadcast(gameID, notificationJson, rootClient);
            if (session.isOpen()) {
                String rootMsg = String.format("\nYou resigned from game: %s, and therefore forfeit the game. YOU LOST!!\n", gameID);
                Notification notificationRoot = new Notification(rootMsg);
                String notificationJsonRoot = gson.toJson(notificationRoot);
                session.getRemote().sendString(notificationJsonRoot);
            }
        } else {
            // Send an Error serverMessage
            Error error = new Error("Error in resigning\n");
            String errorJson = gson.toJson(error);
            if (session.isOpen()) {
                session.getRemote().sendString(errorJson);
            }
        }
    }

    // ----------------------------------------- Type Adapters ----------------------------------------------
    // --------------------------------- For ChessMove && ChessPosition -------------------------------------
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

    // -------------------------------------------------- END ------------------------------------------------
}
