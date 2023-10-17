package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.LoginRequest;
import result.LoginResult;

/** Service responsible to handle user logins. */
public class LoginService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Constructs the log in service object for the user in the game.
     @param authDAO - The users authorization token in the game after logging in.
     @param gameDAO - The game object for this specific game.
     @param userDAO - The object responsible for the users information from the database. */
    public LoginService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /** Attempts to log in an existing user, along with returning an authToken
     * @param request - The request from the user trying to log in
     * */


    // HAVE TO PASS IN REQUEST
    /** This function is in charge of handling the request from the user that is trying to log in.
     * @param request - The log in request from the user.
     * */
    public LoginResult login(LoginRequest request) {
        return null;
    }
}
