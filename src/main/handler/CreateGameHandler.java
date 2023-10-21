package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;
import spark.*;

public class CreateGameHandler {

    public Object handler(Request request, Response result) {
        CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
        CreateGameService createGameService = new CreateGameService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest);
        result.type("application/json");

        if (createGameResult.getMessage() == null) {
            result.status(200);
        }
        else if (createGameResult.getMessage().equals("Error: bad request")) {
            result.status(400);
        }
        else if (createGameResult.getMessage().equals("Error: unauthorized")) {
            result.status (401);
        } else {
            result.status(500);
        }

        return new Gson().toJson(result);
    }
}
