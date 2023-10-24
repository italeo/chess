package service;

import dao.*;
import result.*;

/** This class is responsible to handle the clearing of the game/board
 * from the results that's returned from the results class. */
public class ClearService {

    /** This variable is in charge of setting the game object.*/
    private final GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private final UserDAO userDAO;

    /** Constructs the service necessary to clear the game according to the users request.
     * @param userDAO - The user object containing the necessary information.
     * @param gameDAO - The game object that we are trying to clear.
     * */
    public ClearService(GameDAO gameDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    /**
     * Function that performs the clearing of the results.
     */
 public ClearResult clear() {
     ClearResult result = new ClearResult();

     try {
         gameDAO.clear();
         userDAO.clear();

         result.setSuccess(true);
         result.setMessage("User and Game successfully cleared!");

     } catch (DataAccessException exc) {
         result.setSuccess(false);
         result.setMessage("Failed to clear the game");
     }
     return result;
 }
}
