package dao;

import chess.*;
import dataAccess.DataAccessException;
import com.google.gson.*;
import model.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/** Responsible for accessing the Games that are available from the database. */
public class GameDAO {

    /** Establishes the connection between the Server and the database .*/
    private Connection conn;

    /** Constructs the connection between the Server and the database to access the information needed for the games available
     * in the database
     * /@param conn - associated connection for data access.
     * */
    public GameDAO() {
    }

    // Constructs the connection between the db and the game DAO
    public GameDAO(Connection conn) {
        this.conn = conn;
    }

    /** This function is responsible for inserting games that we have created into the database.
     * @param game - The game the user wishes to store in the database.
     * @throws DataAccessException - thrown when there is an error with the games in the database.
     * */
    public void insert(Game game) throws DataAccessException {
       String sql = "INSERT INTO Game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
       try (PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setInt(1, game.getGameID());
           stmt.setString(2, game.getWhiteUsername());
           stmt.setString(3, game.getBlackUsername());
           stmt.setString(4, game.getGameName());

           // Serialize and store game type ChessGame as JSON
           var json = new Gson().toJson(game.getGame());
           stmt.setString(5, json);
           // Execute the update
           stmt.executeUpdate();

       } catch (SQLException e) {
           throw new DataAccessException("Error with inserting a game: " + e.getMessage());
       }
    }

    /** Clear the game or games stored in the database, done when the user wants to remove the game(s).
     * Or when the user is removed from the database.
     * @throws DataAccessException - thrown when there is an error with clearing the game(s) from the database.
     * */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Game";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing the Game table: " + e.getMessage());
        }
    }

    /** Retrieves a specific game from the database specified by the gameID.
     * @param gameID - The specific ID used to represent a game in the database */
    public Game findGameByID(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM Game WHERE gameID = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int myGameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");

                    // Retrieves the json as a string
                    String json = rs.getString("game");

                    // Register the adapters to convert the string to type ChessGame
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());

                    ChessGame game = builder.create().fromJson(json, ChessGameImpl.class);
                    return new Game(myGameID, whiteUsername, blackUsername, gameName, game);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding a game by gameID: " + e.getMessage());
        }
        return null; // Returns null if game is not found.
    }

    // Updates the game, this is specifically used when a user joins the game, so we need it to update the white/black usernames
    public void updateGame(Game game) throws DataAccessException {
        String sql = "UPDATE Game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game.getWhiteUsername());
            stmt.setString(2, game.getBlackUsername());
            stmt.setString(3, game.getGameName());

            String json = new Gson().toJson(game.getGame());

            stmt.setString(4, json);
            stmt.setInt(5, game.getGameID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error with updating the game: " +e.getMessage());
        }
    }

    // Return a list of all the games created so far
    public List<Game> getAllGames() throws DataAccessException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Game";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");

                // Retrieve the json data
                var json = rs.getString("game");

                // Deserialize the json
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(ChessGame.class, new GameAdapter());
                builder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());
                builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());

                ChessGame game = builder.create().fromJson(json, ChessGameImpl.class);
                games.add(new Game(gameID, whiteUsername, blackUsername, gameName, game));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing the games" + e.getMessage());
        }
        return games;
    }

    // ------------------------------- TYPE ADAPTER CLASSES -------------------------------------------------------

    // Type adapter for ChessGame need to change to JsonSerializer  n
    public static class GameAdapter implements JsonDeserializer<ChessGame> {
        @Override
        public ChessGame deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return new Gson().fromJson(el, ChessGameImpl.class);
        }
    }

    // Type adapter for ChessBoard
    public static class BoardAdapter implements JsonDeserializer<ChessBoard> {
        @Override
        public ChessBoard deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            var builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());

            return builder.create().fromJson(el, ChessBoardImpl.class);
        }
    }


    public static class PieceAdapter implements JsonDeserializer<ChessPiece> {


        @Override
        public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String pieceType = jsonElement.getAsJsonObject().get("type").getAsString();
            Gson gson = new Gson();
            return switch (pieceType) {
                        case "KING" -> gson.fromJson(jsonElement, KingPieceImpl.class);
                        case "QUEEN" -> gson.fromJson(jsonElement, QueenPieceImpl.class);
                        case "KNIGHT" -> gson.fromJson(jsonElement, KnightPieceImpl.class);
                        case "BISHOP" -> gson.fromJson(jsonElement, BishopPieceImpl.class);
                        case "ROOK" -> gson.fromJson(jsonElement, RookPieceImpl.class);
                        case "PAWN" -> gson.fromJson(jsonElement, PawnPieceImpl.class);
                        default -> throw new JsonParseException("Unknown chess piece type" + pieceType);
            };
        }
    }
}
