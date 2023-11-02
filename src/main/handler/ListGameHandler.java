package handler;

import com.google.gson.Gson;
import dao.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import request.ListGamesRequest;
import result.ListGameResult;
import service.ListGamesService;
import spark.*;

import java.sql.Connection;

/** Handles the http request for listing all the games available*/
public class ListGameHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the listGame request.
    public Object handle(Request request, Response response) throws DataAccessException {
        Connection conn = new Database().getConnection();
        ListGamesRequest listGamesRequest = new ListGamesRequest(request.headers("authorization"));
        ListGamesService service = new ListGamesService(new AuthTokenDAO(conn), new GameDAO(conn));
        ListGameResult result = service.listAvailableGames(listGamesRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if (result.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }

        return new Gson().toJson(result);
    }
}
