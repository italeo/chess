package dao;

import chess.*;
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
           // Set the actual game. Have to SERIALIZE FIRST

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

                    // Retrieves the json
                    var json = rs.getString("game");

                    // Converts the json to its specific classes
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessGame.class, new gameAdapter());
                    builder.registerTypeAdapter(ChessBoard.class, new bordAdapter());
                    builder.registerTypeAdapter(ChessPiece.class, new pieceAdapter());

                    var game = builder.create().fromJson(json, ChessGame.class);
                    return new Game(myGameID, whiteUsername, blackUsername, gameName, game);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Error finding a game by gameID: " + e.getMessage());
        }
        return null; // Returns null if game is not found.
    }

    public void claimSpot(int gameID, String username) throws DataAccessException {
    }

    public void removeGame(int gameID) throws DataAccessException {
        String sql = "DELETE FROM Game WHERE gameID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting the game from the table");
        }
    }

    // Updates the game, this is specifically used when a user joins the game, so we need it to update the white/black usernames
    public void updateGame(Game game) throws DataAccessException {
        String sql = "UPDATE Game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game.getWhiteUsername());
            stmt.setString(2, game.getBlackUsername());
            stmt.setString(3, game.getGameName());

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessGame.class, new gameAdapter());
            builder.registerTypeAdapter(ChessBoard.class, new bordAdapter());
            builder.registerTypeAdapter(ChessPiece.class, new pieceAdapter());
            builder.create();
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
                builder.registerTypeAdapter(ChessGame.class, new gameAdapter());
                builder.registerTypeAdapter(ChessBoard.class, new bordAdapter());
                builder.registerTypeAdapter(ChessPiece.class, new pieceAdapter());
                builder.create();

                ChessGame game = new Gson().fromJson(json, ChessGame.class);

                games.add(new Game(gameID, whiteUsername, blackUsername, gameName, game));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing the games" + e.getMessage());
        }
        return games;
    }

    // ------------------------------- TYPE ADAPTER CLASSES -------------------------------------------------------
    static class gameAdapter implements JsonDeserializer<ChessGame> {
        public ChessGame deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return new Gson().fromJson(el, ChessGameImpl.class);
        }
    }

    static class bordAdapter implements JsonDeserializer<ChessBoard> {
        public ChessBoard deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return new Gson().fromJson(el, ChessBoardImpl.class);
        }
    }

    static class pieceAdapter implements  JsonDeserializer<ChessPiece> {
        public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {

            JsonObject jsonObject = el.getAsJsonObject();
            String typeString = jsonObject.get("type").getAsString();

            return switch (typeString) {
                case "KING" -> ctx.deserialize(el, KingPieceImpl.class);
                case "QUEEN" -> ctx.deserialize(el, QueenPieceImpl.class);
                case "KNIGHT" -> ctx.deserialize(el, KnightPieceImpl.class);
                case "ROOK" -> ctx.deserialize(el, RookPieceImpl.class);
                case "BISHOP" -> ctx.deserialize(el, BishopPieceImpl.class);
                case "PAWN" -> ctx.deserialize(el, PawnPieceImpl.class);
                default -> throw new JsonParseException("Unknown chess piece type: " + typeString);
            };
        }
    }
}
