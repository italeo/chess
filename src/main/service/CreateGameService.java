package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.CreateGameRequest;
import result.CreateGameResult;

/** This class is responsible to create a new chess game. */
public class CreateGameService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Contracts a new game service that handles the request and results.
     * @param authDAO - The auth token of the user from the database.
     * @param gameDAO - The game object from the database.
     * @param userDAO - The users object containing the user's necessary information.
     * */
    public CreateGameService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /** Creates a new Chess game with the default settings stated from the rules, and from the game request.
     * @param request - The request from the user to creat a new game.
     * */
    public CreateGameResult createNewGame(CreateGameRequest request) {
        return null;
    }
}
