package dao;

import chess.ChessGame;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/** Responsible for accessing the Games that are available from the database. */
public class GameDAO {

    /** Establishes the connection between the server and the database .*/
    private final Connection conn;

    /** Constructs the connection between the server and the database to access the information needed for the games available
     * in the database
     * @param conn - associated connection for data access.
     * */
    public GameDAO(Connection conn) {
        this.conn = conn;
    }

    /** This function is responsible for inserting games that we have created into the database.
     * @param game - The game the user wishes to store in the database.
     * @throws DatabaseException - thrown when there is an error with the games in the database.
     * */
    public void insert(ChessGame game) throws DatabaseException {
    }

    /** Clear the game or games stored in the database, done when the user wants to remove the game(s).
     * Or when the user is removed from the database.
     * @throws DatabaseException - thrown when there is an error with clearing the game(s) from the database.
     * */
    public void clear() throws DatabaseException{
    }

    /** Retrieves a specific game from the database specified by the gameID.
     * @param gameID - The specific ID used to represent a game in the database
     * @throws DatabaseException - thrown when there is an error retrieving the specific game.
     * */
    public ChessGame findGameByID(int gameID) throws DatabaseException {
        return null;
    }

    /** Retrieves a list of games that the user is trying to access
     * @throws DatabaseException - thrown when there is an error trying to access the list of games from the database.
     * */
    public List<ChessGame> findAllGames() throws DatabaseException {
        return new ArrayList<>();
    }

    /** Allows the user to claim a spot in a game specified by the gameID and the users username
     * @param username  - Represents the user in the game.
     * @param gameID - The specific game the user is trying to claim a spot in.
     * @throws DatabaseException - Thrown when there is an error claiming a spot in the game from the database.
     * */
    public void claimSpot(int gameID, String username) throws DatabaseException {
    }

    /** Updates a chess game from the database.
     * @param gameID - The specific game being updated.
     * @param username - The user trying to update the game.
     * @throws DatabaseException - Thrown when there is an error updating the game from the database.
     * */
    public void updateGame(int gameID, String username) throws DatabaseException {
    }

    /** Removes a specific game from the database.
     * @param gameID - The game being removed.
     * @throws DatabaseException - Thrown when there is an error removing the game from the database.
     * */
    public void removeGame(int gameID) throws DatabaseException {
    }
}
