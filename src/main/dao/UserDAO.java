package dao;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Responsible for accessing the users information from the database.*/
public class UserDAO {

    private Connection conn;

    public UserDAO() {}

    /** Constructs the connection between the database and the Server.
     * /@param conn - Associated connection for data access.
     * */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /** Inserts a user into the database when a new user has registered or when the user logs into the game.
     * @param user - The user being inserted into the game.
     * @throws DataAccessException - Thrown when there is an error adding the user into the database.
     * */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO User (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user into User table: " + e.getMessage());
        }
    }

    /** Clears the user table from the database.
     * @throws DataAccessException - Thrown when there is an error clearing the user table.*/
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing the User table: " + e.getMessage());
        }
    }

    /** Allows the user to query the database to find a specific user,
     * @param username - The username the user is trying to find.
     * @throws DataAccessException - Thrown when there is an error finding the username.
     * */
    public User find(String username) throws DataAccessException {
        String sql = "SELECT * FROM User WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error with querying the User table: " + e.getMessage());
        }
        return null;
    }
}
