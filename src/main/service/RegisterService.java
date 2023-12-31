package service;

import dao.*;
import model.*;
import request.*;
import result.*;
import dataAccess.DataAccessException;

import java.util.UUID;

/** Service for new user registration. */
public class RegisterService {

    /** This variable is the logged-in user's authToken.*/
    private AuthTokenDAO authDAO;
    /** This variable is represents the user object in the game.*/
    private UserDAO userDAO;

    public RegisterService() {
    }

    /** Constructs the registering object when a new user is trying to create an account.
     * @param authDAO - The authToken assigned from the database.
     * @param userDAO - The user object that is stored in the database.
     * */


    public RegisterService(AuthTokenDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) throws dataAccess.DataAccessException {
        RegisterResult result = new RegisterResult();
        User newUser = new User();

        if (!validRequest(request)) {
            result.setMessage("Error: bad request");
            result.setSuccess(false);
            return result;
        }

        try {
            newUser.setUsername(request.getUsername());
            newUser.setPassword(request.getPassword());
            newUser.setEmail(request.getEmail());
            AuthToken authToken = generateAuthToken(request.getUsername());

            User existingUser = userDAO.find(newUser.getUsername());

            if (existingUser == null) {

                // Sets the valid information that the user has provided and added to the database
                userDAO.insert(newUser);
                authDAO.insert(authToken);

                result.setUsername(newUser.getUsername());
                result.setPassword(newUser.getPassword());
                result.setEmail(newUser.getEmail());
                result.setAuthToken(authToken.getAuthToken());
                result.setSuccess(true);

            } else {
                result.setMessage("Error: already taken");
                result.setSuccess(false);
                return result;
            }
        } catch (DataAccessException exc) {
            result.setMessage("Failed to register the user: " + request.getUsername());
        }
        return result;
    }

    // Checks to see that the user that is registering is doing so with valid information
    private boolean validRequest(RegisterRequest request) {
        return request.getEmail() != null &&
                request.getUsername() != null &&
                request.getPassword() != null;
    }

    private AuthToken generateAuthToken(String username) {
        String token = UUID.randomUUID().toString();
        return new AuthToken(token, username);
    }
}
