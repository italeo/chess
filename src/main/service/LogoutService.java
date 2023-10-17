package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import result.LogoutResult;

/** Service responsible for user logout. */
public class LogoutService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Constructs the logout service object for the user in the game.
     @param authDAO - The users authorization token in the game.
     @param gameDAO - The game object for this specific game.
     @param userDAO - The object responsible for the users information from the database. */
    public LogoutService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /** Logs the user out of the game.
     * */
    public LogoutResult logout() {
        return null;
    }
}
