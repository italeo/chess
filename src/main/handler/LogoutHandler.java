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
public class LogoutHandler implements Route {
    public Object handle(Request request, Response response) {
        LogoutRequest logoutRequest = new Gson().fromJson(request.body(), LogoutRequest.class);
        LogoutService service = new LogoutService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        LogoutResult result = service.logout(logoutRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if(result.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }

        return new Gson().toJson(result);
    }
}
