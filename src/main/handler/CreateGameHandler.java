package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;
import spark.*;

public class CreateGameHandler implements Route {

    public Object handle(Request request, Response response) {
        CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
        CreateGameService service = new CreateGameService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        CreateGameResult result = service.createGame(createGameRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if (result.getMessage().equals("Error: bad request")) {
            response.status(400);
        }
        else if (result.getMessage().equals("Error: unauthorized")) {
            response.status (401);
        } else {
            response.status(500);
        }

        return new Gson().toJson(response);
    }
}
