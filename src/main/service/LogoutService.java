package service;

import dao.*;
import dataAccess.Database;
import request.*;
import result.*;

/** Service responsible for user logout. */
public class LogoutService {

    /** This variable is the logged-in user's authToken.*/
    private AuthTokenDAO authDAO;

    /** Constructs the logout service object for the user in the game.
     @param authDAO - The users authorization token in the game.
     */
    public LogoutService(AuthTokenDAO authDAO) {
        this.authDAO = authDAO;
    }

    /** Logs the user out of the game.
     * */
    public LogoutResult logout(LogoutRequest request) throws dataAccess.DataAccessException {
        LogoutResult result = new LogoutResult();

        if (!validRequest(request)) {
            result.setMessage("Error: unauthorized");
            result.setSuccess(false);
            return result;
        }

        try {
            if (authDAO.find(request.getAuthToken()) != null) {
                authDAO.delete(request.getAuthToken());
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setMessage("Error: unauthorized");
            }

        } catch (DataAccessException exc) {
            result.setMessage("Failed to logout");
        }
        return result;
    }

    private boolean validRequest(LogoutRequest request) {
        return request.getAuthToken() != null;
    }

}
