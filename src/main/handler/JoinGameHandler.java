package handler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.*;
import request.*;
import result.*;
import service.*;
import spark.*;

/** Responsible for handling the http request from a user to join a game. */
public class JoinGameHandler implements Route {

    public Object handle(Request request, Response response) {


        JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);

        joinGameRequest.setAuthToken(request.headers("authorization"));
        JoinGameService service = new JoinGameService(new AuthTokenDAO(), new GameDAO());
        JoinGameResult result = service.joinGame(joinGameRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if (result.getMessage().equals("Error: bad request")) {
            response.status(400);
        }
        else if (result.getMessage().equals("Error: unauthorized")) {
            response.status (401);
        }
        else if (result.getMessage().equals("Error: already taken")) {
            response.status(403);
        }else {
            response.status(500);
        }
        return new Gson().toJson(result);
    }
}
