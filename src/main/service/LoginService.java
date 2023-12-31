package service;

import dao.*;
import model.*;
import request.*;
import result.*;
import java.util.UUID;
import dataAccess.DataAccessException;

/** Service responsible to handle user logins. */
public class LoginService {

    /** This variable is the logged-in user's authToken.*/
    private AuthTokenDAO authDAO;
    /** This variable is represents the user object in the game.*/
    private UserDAO userDAO;

    /** Constructs the log in service object for the user in the game.
     @param authDAO - The users authorization token in the game after logging in.
     @param userDAO - The object responsible for the users information from the database. */
    public LoginService(AuthTokenDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    /** This function is in charge of handling the request from the user that is trying to log in.
     * @param request - The log in request from the user.
     * */
    public LoginResult login(LoginRequest request) throws dataAccess.DataAccessException {
        LoginResult result = new LoginResult();

        if (!validRequest(request)) {
            result.setMessage("Error: unauthorized");
            return result;
        }

        try {
            User user = userDAO.find(request.getUsername());

            if (user != null && user.getUsername().equals(request.getUsername()) &&
                    user.getPassword().equals(request.getPassword())) {

                // Generates an authToken for a valid user and adds that to the database
                AuthToken authToken = generateAuthToken(user.getUsername());
                authDAO.insert(authToken);

                // Sets the necessary information
                result.setUsername(user.getUsername());
                result.setAuthToken(authToken.getAuthToken());
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setMessage("Error: unauthorized");
                return result;
            }

        } catch (DataAccessException exc) {
            exc.printStackTrace();
            result.setMessage("Error: server error");
        }

        return result;
    }

    private AuthToken generateAuthToken(String username) {
        String token = UUID.randomUUID().toString();
        return new AuthToken(token, username);
    }

    private boolean validRequest(LoginRequest request) {
        return request.getPassword() != null &&
                request.getUsername() != null;
    }
}
