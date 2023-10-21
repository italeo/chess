package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import spark.*;


public class LoginHandler {

    public Object handle(Request request, Response result) {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService logoutService = new LoginService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        LoginResult loginResult = logoutService.login(loginRequest);

        if (loginResult.getMessage() == null) {
            result.status(200);
        }
        else if (loginResult.getMessage().equals("Error: unauthorized")) {
            result.status(401);
        } else {
            result.status(500);
        }

        return new Gson().toJson(loginResult);
    }
}
