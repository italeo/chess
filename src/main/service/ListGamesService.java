package service;

import dao.*;
import request.*;
import result.*;
import model.*;
import java.util.*;

/** The service responsible for listing all chess games available. */
public class ListGamesService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;

    /**Constructs the object responsible to list out the games.
     * @param gameDAO - The object containing the necessary information about the game from the database.
     * @param authDAO - The authorization token from the database.
     * */
    public ListGamesService(AuthTokenDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /** This function lists the games that are available from the database.
     * @param request - Request from the user to get the list of games.
     * */
    public ListGameResult listAvailableGames(ListGamesRequest request) {
        ListGameResult result = new ListGameResult();
        List<ListGameSuccessResult> results = new ArrayList<>();
        result.setSuccess(true);

        try {
            if (authDAO.find(request.getAuthToken()) != null) {
                List<Game> gamesAvailable = gameDAO.getAllGames();

                for (Game game : gamesAvailable) {
                    ListGameSuccessResult gamesResult = new ListGameSuccessResult();

                    gamesResult.setGameID(game.getGameID());
                    gamesResult.setGameName(game.getGameName());
                    gamesResult.setBlackUsername(game.getBlackUsername());
                    gamesResult.setWhiteUsername(game.getWhiteUsername());

                    results.add(gamesResult);
                }

                result.setGames(results);

            } else {
                result.setMessage("Error: unauthorized");
                result.setSuccess(false);
                return result;
            }

        } catch (Exception exc) {
            result.setSuccess(false);
            result.setMessage("Error: unauthorized");
            return result;
        }
        return result;
    }
}
