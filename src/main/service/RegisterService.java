package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.RegisterRequest;
import result.RegisterResult;

/** Service for new user registration. */
public class RegisterService {
    /** Constructs are new user with the provided username, password and email.
     * */

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Constructs the registering object when a new user is trying to create an account.
     * @param authDAO - The authToken assigned from the database.
     * @param userDAO - The user object that is stored in the database.
     * @param gameDAO - The game object containing the information for the requested game that is stored in the database.
     * */
    public RegisterService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest request) {
        RegisterResult result = new RegisterResult();


        return null;
    }

    // FINISH IMPLEMENTING THIS FUNCTION
    private boolean validRequest(RegisterRequest request) {
        return false;
    }
}
