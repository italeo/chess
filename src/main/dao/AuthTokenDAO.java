package dao;

import model.AuthToken;
import java.util.HashMap;
import java.util.Map;

/** Responsible for accessing the data for the AuthToken */
public class AuthTokenDAO {
    private static Map<String, AuthToken> authTokenMap = new HashMap<>();

    /** Constructs the connection between the Server and the database to access the information needed for the authToken
     //* @param conn - associated connection for data access.
     * */
    public AuthTokenDAO() {
        //this.authTokenMap = new HashMap<>();
    }

    /** This function will insert an authToken for a user creates a new account or logs in.
     * @param authToken - The unique token used to represent the user.
     * @throws DataAccessException - thrown when there is a database error.
     * */
    public void insert(AuthToken authToken) throws DataAccessException {
        authTokenMap.put(authToken.getAuthToken(), authToken);
    }

    /** This clear function removes the authorization token from the database that belongs to the user.
     * This only occurs when the user is removed from the database.
     * @throws DataAccessException - thrown when there is a database error.
     * */
    public void clear() throws DataAccessException {
        authTokenMap.clear();
    }

    /** This function allows us to query the  right table in the database for the specific authorization token specified by:
     * @param authToken - The specific authToken we are trying to find.
     * @throws DataAccessException - thrown when there is a database error.*/
    public AuthToken find(String authToken) throws DataAccessException {
        return authTokenMap.get(authToken);
    }

    // Modified this function from phase-2 to properly clear the hashMap
    public void delete(String token) throws DataAccessException {
        authTokenMap.remove(token);
    }
}
