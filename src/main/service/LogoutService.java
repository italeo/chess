package service;

import dao.*;
import model.*;
import request.*;
import result.*;

/** Service responsible for user logout. */
public class LogoutService {

    /** This variable is the logged-in user's authToken.*/
    private final AuthTokenDAO authDAO;

    /** Constructs the logout service object for the user in the game.
     @param authDAO - The users authorization token in the game.
     */
    public LogoutService(AuthTokenDAO authDAO) {
        this.authDAO = authDAO;
    }

    /** Logs the user out of the game.
     * */
    public LogoutResult logout(LogoutRequest request) {
        LogoutResult result = new LogoutResult();
        try {
            AuthToken authToken = authDAO.find(request.getAuthToken());

            // LOGICAL ERROR HERE??
            if (authToken != null) {
                authDAO.delete(request.getAuthToken());
            } else {
                result.setMessage("Error: unauthorized");
            }

        } catch (DataAccessException exc) {
            result.setMessage("Failed to logout");
        }
        return result;
    }

}
