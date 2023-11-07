package dao;

import model.AuthToken;
import dataAccess.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Responsible for accessing the data for the AuthToken */
public class AuthTokenDAO {
    // Used to establish the connection
    private Connection conn;

    public AuthTokenDAO() {
    }

    /** Constructs the connection between the Server and the database to access the information needed for the authToken
     //* @param conn - associated connection for data access.
     * */

    // Constructs the connection to the authToken DAO
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /** This function will insert an authToken for a user creates a new account or logs in.
     * @param authToken - The unique token used to represent the user.
     * @throws DataAccessException - thrown when there is a database error.
     * */
    public void insert(AuthToken authToken) throws DataAccessException {

        String sql = "INSERT INTO AuthToken (authToken, username) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while inserting into AuthToken table: " + e.getMessage());
        }

    }

    /** This clear function removes the authorization token from the database that belongs to the user.
     * This only occurs when the user is removed from the database.
     * @throws DataAccessException - thrown when there is a database error.
     * */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthToken";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error while try to clear AuthToken table: " + e.getMessage());
        }
    }

    /** This function allows us to query the  right table in the database for the specific authorization token specified by:
     * @param authToken - The specific authToken we are trying to find.
     * @throws DataAccessException - thrown when there is a database error.*/
    public AuthToken find(String authToken) throws DataAccessException {

        String sql = "SELECT * FROM AuthToken WHERE authToken = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AuthToken(rs.getString("authToken"), rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while querying the AuthToken table: " + e.getMessage());
        }
        return null;
    }

    // Deletes an individual authToken from the db, this is mainly used for logging a user out
    public void delete(String authToken) throws DataAccessException {

        String sql = "DELETE FROM AuthToken WHERE authToken = ?";
        try {
            if (authToken != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, authToken);
                    stmt.executeUpdate();
                }
            } else {
                throw new DataAccessException("AuthToken does not exist");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error with deleting an authToken from AuthToken table: " + e.getMessage());
        }
    }
}
