package dao;

import model.User;
import java.util.HashMap;
import java.util.Map;

/** Responsible for accessing the users information from the database.*/
public class UserDAO {
    /** Used to establish the connection between Server and database.*/
   // private final Connection conn;
    private final static Map<String, User> userMap = new HashMap<>();

    /** Constructs the connection between the database and the Server.
     * /@param conn - Associated connection for data access.
     * */
    public UserDAO() {
        //this.userMap = new HashMap<>();
    }

    /** Inserts a user into the database when a new user has registered or when the user logs into the game.
     * @param user - The user being inserted into the game.
     * @throws DataAccessException - Thrown when there is an error adding the user into the database.
     * */
    public void insert(User user) throws DataAccessException {
        userMap.put(user.getUsername(), user);
    }

    /** Clears the user table from the database.
     * @throws DataAccessException - Thrown when there is an error clearing the user table.*/
    public void clear() throws DataAccessException {
        userMap.clear();
    }

    /** Allows the user to query the database to find a specific user,
     * @param username - The username the user is trying to find.
     * @throws DataAccessException - Thrown when there is an error finding the username.
     * */
    public User find(String username) throws DataAccessException {
        return userMap.get(username);
    }
}
