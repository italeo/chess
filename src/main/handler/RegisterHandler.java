package handler;


import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;
import spark.*;

public class RegisterHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the Register request.
    public Object handle(Request request, Response response) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
        RegisterService service = new RegisterService(new AuthTokenDAO(), new UserDAO());
        RegisterResult result = service.register(registerRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if (result.getMessage().equalsIgnoreCase("Error: bad request")) {
            response.status(400);
        } else if (result.getMessage().equalsIgnoreCase("Error: already taken")) {
            response.status(403);
        } else {
            response.status(500);
        }

        return new Gson().toJson(result);
    }
}
