package dao;

import dataAccess.Database;
import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/** Responsible for accessing the data for the AuthToken */
public class AuthTokenDAO {
    private final Database db;

    /** Constructs the connection between the Server and the database to access the information needed for the authToken
     //* @param conn - associated connection for data access.
     * */


    public AuthTokenDAO(Database db) {
        this.db = db;
    }

    /** This function will insert an authToken for a user creates a new account or logs in.
     * @param authToken - The unique token used to represent the user.
     * @throws DataAccessException - thrown when there is a database error.
     * */
    public void insert(AuthToken authToken) throws DataAccessException {
        try(Connection conn = db.getConnection()) {
            String sql = "INSERT INTO AuthToken (token, username) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, authToken.getAuthToken());
                stmt.setString(2, authToken.getUsername());
                stmt.executeUpdate();
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new DataAccessException("Error while inserting authToken: " + e.getMessage());
        }
    }

    /** This clear function removes the authorization token from the database that belongs to the user.
     * This only occurs when the user is removed from the database.
     * @throws DataAccessException - thrown when there is a database error.
     * */
    public void clear() throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            String sql = "DELETE FROM AuthToken";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new DataAccessException("Error while try to clear AuthToken table: " + e.getMessage());
        }

    }

    /** This function allows us to query the  right table in the database for the specific authorization token specified by:
     * @param authToken - The specific authToken we are trying to find.
     * @throws DataAccessException - thrown when there is a database error.*/
    public AuthToken find(String authToken) throws DataAccessException {
        return null;
    }

    // Modified this function from phase-2 to properly clear the hashMap
    public void delete(String token) throws DataAccessException {

    }
}
