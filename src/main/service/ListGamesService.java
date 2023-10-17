package service;

import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.ListGamesRequest;
import result.ListGameResult;

/** The service responsible for listing all chess games available. */
public class ListGamesService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /**Constructs the object responsible to list out the games.
     * @param gameDAO - The object containing the necessary information about the game from the database.
     * @param userDAO - The object stores the information for the logged-in user.
     * @param authDAO - The authorization token from the database.
     * */
    public ListGamesService(AuthTokenDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /** This function lists the games that are available from the database.
     * @param request - Request from the user to get the list of games.
     * */
    public ListGameResult listAvailableGames(ListGamesRequest request) {
        return null;
    }
}
