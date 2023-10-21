package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import model.AuthToken;
import result.ClearResult;

/** This class is responsible to handle the clearing of the game/board
 * from the results that's returned from the results class. */
public class ClearService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Constructs the service necessary to clear the game according to the users request.
     * @param userDAO - The user object containing the necessary information.
     * @param gameDAO - The game object that we are trying to clear.
     * @param authDAO - The users authorization token from the database.
     * */
    public ClearService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /** Function that performs the clearing of the results. */
 public ClearResult clear() {
     return null;
 }
}
