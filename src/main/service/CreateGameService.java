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
        int gameID;

        if (!validRequest(request)) {
            result.setMessage("Error: bad request");
            return result;
        }

        result.setGameName(result.getGameName());

        try {

            // Create a gameID
            Random random = new Random();
            gameID = random.nextInt(1000);
            Game newGame = new Game();
            result.setGameID(gameID);
            gameDAO.insert(newGame);

        } catch (Exception exc) {
            result.setMessage("Error: Server error");
        }


        return result;
    }

    private boolean validRequest(CreateGameRequest request) {
        return request.getGameName() != null;
    }
}
