package service;

import dao.*;
import dataAccess.Database;
import result.*;

/** This class is responsible to handle the clearing of the game/board
 * from the results that's returned from the results class. */
public class ClearService {

    /** This variable is in charge of setting the game object.*/
    private GameDAO gameDAO;
    /** This variable is represents the user object in the game.*/
    private UserDAO userDAO;
    private AuthTokenDAO authTokenDAO;

    /** Constructs the service necessary to clear the game according to the users request.
     * @param userDAO - The user object containing the necessary information.
     * @param gameDAO - The game object that we are trying to clear.
     * */
    public ClearService(GameDAO gameDAO, UserDAO userDAO, AuthTokenDAO authTokenDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authTokenDAO = authTokenDAO;
    }

    /**
     * Function that performs the clearing of the results.
     */
 public ClearResult clear() throws dataAccess.DataAccessException {
     ClearResult result = new ClearResult();
     Database db = new Database();
     authTokenDAO = new AuthTokenDAO(db.getConnection());
     gameDAO = new GameDAO(db.getConnection());
     userDAO = new UserDAO(db.getConnection());

     try {
         gameDAO.clear();
         userDAO.clear();
         authTokenDAO.clear();

         result.setSuccess(true);
         result.setMessage("User and Game successfully cleared!");

     } catch (DataAccessException exc) {
         result.setSuccess(false);
         result.setMessage("Failed to clear the game");
     }
     return result;
 }
}
