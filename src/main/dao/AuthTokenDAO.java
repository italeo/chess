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

    /** This clear function removes the authorization token from the database that belongs to the user.
     * This only occurs when the user is removed from the database.
     * @throws DatabaseException - thrown when there is a database error.
     * */
    public void clear() throws DatabaseException {
    }

    /** This function allows us to query the  right table in the database for the specific authorization token specified by:
     * @param authToken - The specific authToken we are trying to find.
     * @throws DatabaseException - thrown when there is a database error.*/
    public AuthToken find(String authToken) throws DatabaseException {
        return null;
    }
}
