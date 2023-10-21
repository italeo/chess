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
public class JoinGameHandler {

    public Object handle(Request request, Response result) {
        JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        JoinGameService joinGameService = new JoinGameService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        JoinGameResult joinGameResult = joinGameService.joinGame(joinGameRequest);
        result.type("application/json");

        if (joinGameResult.getMessage() == null) {
            result.status(200);
        }
        else if (joinGameResult.getMessage().equals("Error: bad request")) {
            result.status(400);
        }
        else if (joinGameResult.getMessage().equals("Error: unauthorized")) {
            result.status (401);
        }
        else if (joinGameResult.getMessage().equals("Error: Already taken")) {

        }else {
            result.status(500);
        }
        return new Gson().toJson(result);
    }
}
