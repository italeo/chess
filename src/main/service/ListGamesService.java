package service;

import dao.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import request.*;
import result.*;
import model.*;
import java.util.*;

/** The service responsible for listing all chess games available. */
public class ListGamesService {

    /** This variable is the logged-in user's authToken.*/
    private AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private GameDAO gameDAO;

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
    public ListGameResult listAvailableGames(ListGamesRequest request) throws DataAccessException {
        ListGameResult result = new ListGameResult();
        Database db = new Database();
        List<ListGameSuccessResult> results = new ArrayList<>();
        result.setSuccess(true);

        gameDAO = new GameDAO(db.getConnection());
        authDAO = new AuthTokenDAO(db.getConnection());

        try {
            // Validates the request, authToken must not be null and must be in the database
            if (authDAO.find(request.getAuthToken()) != null) {
                List<Game> gamesAvailable = gameDAO.getAllGames();

                // If it is valid it loops through and sets the information that needs to be presented and displays that
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
