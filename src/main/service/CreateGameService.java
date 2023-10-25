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

        if (!validRequest(request)) {
            result.setMessage("Error: bad request");
            return result;
        }
        try {

            if (authDAO.find(request.getAuthToken()) != null) {

                Random random = new Random();
                gameID = random.nextInt(10000);

                newGame.setGameID(gameID);
                gameDAO.insert(newGame);

                result.setGameName(request.getGameName());
                result.setGameID(gameID);

            } else {
                result.setMessage("Error: unauthorized");
                result.setGameID(null);
                return result;
            }
        } catch (Exception exc) {
            result.setMessage("Error: Server error");
            return result;
        }


        return result;
    }

    private boolean validRequest(CreateGameRequest request) {
        return request.getGameName() != null &&
                request.getAuthToken() != null;
    }
}
