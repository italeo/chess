package service;

import dao.*;
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


    public JoinGameResult joinGame(JoinGameRequest request) {
        JoinGameResult result = new JoinGameResult();

        if (!validRequest(request)) {
            result.setMessage("Error: bad request");
            return result;
        }

        try {

            AuthToken authToken = authDAO.find(request.getAuthToken());

           if (authDAO.find(request.getAuthToken()) != null &&
                   gameDAO.findGameByID(request.getGameID()) != null) {

               String teamColor = request.getPlayerColor();

               if (teamColor == null) {
                   result.setSuccess(true);
                   return result;

               } else {

                   Game game = gameDAO.findGameByID(request.getGameID());

                   if (teamColor.equals("WHITE")) {
                       if (game.getWhiteUsername() == null) {
                           game.setWhiteUsername(authToken.getUsername());
                           result.setSuccess(true);
                           gameDAO.updateGame(game);

                       } else {
                           result.setMessage("Error: already taken");
                           result.setSuccess(false);
                           return result;
                       }
                   } else {
                       if (game.getBlackUsername() == null) {
                           game.setBlackUsername(authToken.getUsername());
                           result.setSuccess(true);
                           gameDAO.updateGame(game);

                       } else {
                           result.setSuccess(false);
                           result.setMessage("Error: already taken");
                           return result;
                       }
                   }
               }

               result.setSuccess(true);

           } else {
               result.setSuccess(false);
               result.setMessage("Error: unauthorized");
               return result;
           }
        } catch (Exception exc) {
            result.setSuccess(false);
            result.setMessage("Error: unauthorized");
        }
        return result;
    }

    private boolean validRequest(JoinGameRequest request) {
        return request.getGameID() > 0 &&
                request.getAuthToken() != null;
    }
}
