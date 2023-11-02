package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import spark.*;


public class LoginHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the login request.
    public Object handle(Request request, Response response) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService service = new LoginService(new AuthTokenDAO(), new UserDAO());
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
