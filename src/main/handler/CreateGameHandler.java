package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.*;
import request.*;
import result.*;
import service.*;
import spark.*;


public class CreateGameHandler implements Route {

    public Object handle(Request request, Response response) {

        JsonObject body = new Gson().fromJson(request.body(), JsonObject.class);
        String gameName = body.get("gameName").getAsString();

        CreateGameRequest createGameRequest = new CreateGameRequest(request.headers("authorization"), gameName);
        CreateGameService service = new CreateGameService(new AuthTokenDAO(), new GameDAO());
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

        return new Gson().toJson(result);
    }
}
