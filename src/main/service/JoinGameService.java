package service;

import dao.*;
import dataAccess.DataAccessException;
import model.*;
import request.*;
import result.*;

/** Responsible to allow players to join a game if the game is available. */
public class JoinGameService {

    /** This variable is the logged-in user's authToken.*/
    private AuthTokenDAO authDAO;
    /** This variable is in charge of setting the game object.*/
    private GameDAO gameDAO;

    public JoinGameService() {
    }

    /** Constructs the object of necessary for the user to join the game from the request and produced results.
     * @param authDAO - The users auth token from the database.
     * @param gameDAO - The game object containing the information about the requested game.
     * */


    public JoinGameService(AuthTokenDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException {
        JoinGameResult result = new JoinGameResult();
        result.setSuccess(false);

        // checks if request is valid
        if (!validRequest(request)) {
            result.setMessage("Error: bad request");
            return result;
        }

        try {
            // Retrieves the authToken and the specified game with the gameID
            AuthToken authToken = authDAO.find(request.getAuthToken());
            Game game = gameDAO.findGameByID(request.getGameID());

           if (authToken != null && game != null) {
               String teamColor = request.getPlayerColor();

               // Checks if a team color is specified when the user tries to join the game, if not specified then the user joins as
               // an observer
               if (teamColor == null) {
                   result.setSuccess(true);
                   return result;
               }

               // Handles if a team color is already taken (BLACK/WHITE)
               if (teamColor.equals("WHITE") && game.getWhiteUsername() == null) {
                   game.setWhiteUsername(authToken.getUsername());
               } else if (teamColor.equals("BLACK") && game.getBlackUsername() == null) {
                   game.setBlackUsername(authToken.getUsername());
               } else {
                   result.setMessage("Error: already taken");
                   return result;
               }

               gameDAO.updateGame(game);
               result.setSuccess(true);
           } else {
               result.setMessage("Error: unauthorized");
           }
        } catch (Exception exc) {
            result.setMessage("Error: unauthorized");
        }
        return result;
    }

    private boolean validRequest(JoinGameRequest request) {
        return request.getGameID() > 0 &&
                request.getAuthToken() != null;
    }
}
