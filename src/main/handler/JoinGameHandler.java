package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.JoinGameService;
import spark.*;

import javax.xml.transform.Result;

/** Responsible for handling the http request from a user to join a game. */
public class JoinGameHandler implements Route {

    public Object handle(Request request, Response response) {
        JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        JoinGameService service = new JoinGameService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
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
        else if (result.getMessage().equals("Error: Already taken")) {

        }else {
            response.status(500);
        }
        return new Gson().toJson(response);
    }
}
