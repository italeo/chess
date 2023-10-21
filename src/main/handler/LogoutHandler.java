package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.LogoutRequest;
import result.LoginResult;
import result.LogoutResult;
import service.LogoutService;
import spark.*;

/** Handler for http request from user to logout. */
public class LogoutHandler {
    public Object handle(Request request, Response result) {
        LogoutRequest logoutRequest = new Gson().fromJson(request.body(), LogoutRequest.class);
        LogoutService logoutService = new LogoutService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        LogoutResult logoutResult = logoutService.logout(logoutRequest);
        result.type("application/json");

        if (logoutResult.getMessage() == null) {
            result.status(200);
        }
        else if(logoutResult.getMessage().equals("Error: unauthorized")) {
            result.status(401);
        } else {
            result.status(500);
        }

        return new Gson().toJson(logoutResult);
    }
}
