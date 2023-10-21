package handler;


import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;
import spark.*;

public class RegisterHandler {
    public Object handle(Request request, Response result) {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
        RegisterService registerService = new RegisterService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        RegisterResult registerResult = registerService.register(registerRequest);
        result.type("application/json");

        if (registerResult.getMessage() == null) {
            result.status(200);
        }
        else if (registerResult.getMessage().equals("Error: bad request")) {
            result.status(400);
        } else if (registerResult.getMessage().equals("Error: already taken")) {
            result.status(403);
        } else {
            result.status(500);
        }

        return new Gson().toJson(registerResult);
    }
}
