package dao;

import model.*;
import java.util.*;

/** Responsible for accessing the Games that are available from the database. */
public class GameDAO {

    /** Establishes the connection between the Server and the database .*/
    private static final Map<Integer, Game> gameMap = new HashMap<>();


    /** Constructs the connection between the Server and the database to access the information needed for the games available
     * in the database
     * /@param conn - associated connection for data access.
     * */
    public GameDAO() {
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
     * @param gameID - The specific ID used to represent a game in the database */
    public Game findGameByID(int gameID) {
        return gameMap.get(gameID);
    }

    // Updates the game, this is specifically used when a user joins the game, so we need it to update the white/black usernames
    public void updateGame(Game game) {
        gameMap.put(game.getGameID(), game);
    }

    // Return a list of all the games created so far
    public List<Game> getAllGames() {
        List<Game> allGames = new ArrayList<>(gameMap.values());
        return allGames;
    }
}
