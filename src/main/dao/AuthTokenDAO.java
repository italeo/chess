package dao;

import model.AuthToken;

import java.sql.Connection;

/** Responsible for accessing the data for the AuthToken */
public class AuthTokenDAO {
    /** Creates the connection between the server and the database. */

    private final Connection conn;

    /** Constructs the connection between the server and the database to access the information needed for the authToken
     * @param conn - associated connection for data access.
     * */
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /** This function will insert an authToken for a user creates a new account or logs in.
     * @param authToken - The unique token used to represent the user.
     * @throws DatabaseException - thrown when there is a database error.
     * */
    public void insert(AuthToken authToken) throws DatabaseException {
    }
}
