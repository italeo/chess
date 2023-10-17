package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import result.JoinGameResult;

/** Responsible to allow players to join a game if the game is available. */
public class JoinGameService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Constructs the object of necessary for the user to join the game from the request and produced results.
     * @param authDAO - The users auth token from the database.
     * @param gameDAO - The game object containing the information about the requested game.
     * @param userDAO - The users information from the database.
     * */
    public JoinGameService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /** Checks if the game is available or not. */
    public JoinGameResult isGameAvailable() {
        return null;
    }

}
