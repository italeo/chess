package handler;

import com.google.gson.Gson;
import dao.*;
import dataAccess.DataAccessException;
import request.*;
import result.*;
import service.*;
import spark.*;

// Handles the request we get from the client and returns the response from the server, the request is passed to the service
// where it then returns the results for the creatGame request.
public class CreateGameHandler implements Route {

    public Object handle(Request request, Response response) throws DataAccessException {

        CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);

        createGameRequest.setAuthToken(request.headers("authorization"));
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
