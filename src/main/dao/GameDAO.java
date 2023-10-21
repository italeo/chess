package dao;

import chess.ChessGame;
import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Responsible for accessing the Games that are available from the database. */
public class GameDAO {

    /** Establishes the connection between the Server and the database .*/
    //private final Connection conn;
    private final Map<Integer, Game> gameMap;

    /** Constructs the connection between the Server and the database to access the information needed for the games available
     * in the database
     * /@param conn - associated connection for data access.
     * */
    public GameDAO() {
        this.gameMap = new HashMap<>();
    }

    /** This function is responsible for inserting games that we have created into the database.
     * @param game - The game the user wishes to store in the database.
     * @throws DataAccessException - thrown when there is an error with the games in the database.
     * */
    public void insert(Game game) throws DataAccessException {
        gameMap.put(game.getGameID(), game);
    }

    /** Clear the game or games stored in the database, done when the user wants to remove the game(s).
     * Or when the user is removed from the database.
     * @throws DataAccessException - thrown when there is an error with clearing the game(s) from the database.
     * */
    public void clear() throws DataAccessException {
        gameMap.clear();
    }

    /** Retrieves a specific game from the database specified by the gameID.
     * @param gameID - The specific ID used to represent a game in the database
     * @throws DataAccessException - thrown when there is an error retrieving the specific game.
     * */
    public Game findGameByID(int gameID) throws DataAccessException {
        return gameMap.get(gameID);
    }

    /** Allows the user to claim a spot in a game specified by the gameID and the users username
     * @param username  - Represents the user in the game.
     * @param gameID - The specific game the user is trying to claim a spot in.
     * @throws DataAccessException - Thrown when there is an error claiming a spot in the game from the database.
     * */
    public void claimSpot(int gameID, String username) throws DataAccessException {
    }
    public void updateGame(Game game) throws DataAccessException {
        gameMap.put(game.getGameID(), game);
    }

    public void removeGame(Game game) throws DataAccessException {
        gameMap.remove(game.getGameID(), game);
    }
}
