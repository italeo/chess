package service;

import dao.*;
import model.*;
import request.*;
import result.*;

import java.util.Random;

/** This class is responsible to create a new chess game. */
public class CreateGameService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;
    private final GameDAO gameDAO;

    /** Contracts a new game service that handles the request and results.
     * @param authDAO - The auth token of the user from the database.
     * */
    public CreateGameService(AuthTokenDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /** Creates a new Chess game with the default settings stated from the rules, and from the game request.
     * @param request - The request from the user to creat a new game.
     * */
    public CreateGameResult createGame(CreateGameRequest request) {
        CreateGameResult result = new CreateGameResult();
        Game newGame = new Game();
        int gameID;

        // Checks if the request is valid, if not return the right error message
        if (!validRequest(request)) {
            result.setMessage("Error: bad request");
            result.setSuccess(false);
            return result;
        }
        try {

            if (authDAO.find(request.getAuthToken()) != null) {

                Random random = new Random();
                gameID = random.nextInt(10000);

                newGame.setGameID(gameID);
                newGame.setGameName(request.getGameName());
                gameDAO.insert(newGame);

                result.setGameName(request.getGameName());
                result.setGameID(gameID);
                result.setSuccess(true);

            } else {
                result.setMessage("Error: unauthorized");
                result.setGameID(null);
                result.setSuccess(false);
                return result;
            }
        } catch (Exception exc) {
            result.setMessage("Error: Server error");
            return result;
        }

        return result;
    }

    // Method used to check if the request is valid, and that is checking if the game name and authToken is not null
    private boolean validRequest(CreateGameRequest request) {
        return request.getGameName() != null &&
                request.getAuthToken() != null;
    }
}
