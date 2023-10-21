package handler;


import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import request.ListGamesRequest;
import result.ListGameResult;
import service.ListGamesService;
import spark.*;

/** Handles the http request for listing all the games available*/
public class ListGameHandler {

    public Object handle(Request request, Response result) {
        ListGamesRequest listGamesRequest = new Gson().fromJson(request.body(), ListGamesRequest.class);
        ListGamesService listGamesService = new ListGamesService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        ListGameResult listGameResult = listGamesService.listAvailableGames(listGamesRequest);
        result.type("application/json");

        if (listGameResult.getMessage() == null) {
            result.status(200);
        }
        else if (listGameResult.getMessage().equals("Error: unathorized")) {
            result.status(401);
        } else {
            result.status(500);
        }

        return new Gson().toJson(listGameResult);
    }
}
