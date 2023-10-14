package dao;

import model.User;

import java.sql.Connection;

/** Responsible for accessing the users information from the database.*/
public class UserDAO {
    /** Used to establish the connection between server and database.*/
    private final Connection conn;

    /** Constructs the connection between the database and the server.
     * @param conn - Associated connection for data access.
     * */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /** Inserts a user into the database when a new user has registered or when the user logs into the game.
     * @param user - The user being inserted into the game.
     * @throws DataAccessException - Thrown when there is an error adding the user into the database.
     * */
    public void insert(User user) throws DataAccessException {
    }

    /** Clears the user table from the database.
     * @throws DataAccessException - Thrown when there is an error clearing the user table.*/
    public void clear() throws DataAccessException {
    }

    /** Allows the user to query the database to find a specific user,
     * @param username - The username the user is trying to find.
     * @throws DataAccessException - Thrown when there is an error finding the username.
     * */
    public User find(String username) throws DataAccessException {
        return null;
    }
}
