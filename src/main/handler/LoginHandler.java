package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import spark.*;


public class LoginHandler implements Route {

    public Object handle(Request request, Response response) {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService service = new LoginService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        LoginResult result = service.login(loginRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if (result.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }

        return new Gson().toJson(result);
    }
}
