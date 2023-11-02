package handler;

import com.google.gson.Gson;
import dao.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import request.*;
import result.*;
import service.*;
import spark.*;

import java.sql.Connection;

/** Responsible for handling the http request from a user to join a game. */
public class JoinGameHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the clear request.
    public Object handle(Request request, Response response) throws DataAccessException {

        JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        Database db = new Database();
        Connection conn = new Database().getConnection();
        joinGameRequest.setAuthToken(request.headers("authorization"));
        JoinGameService service = new JoinGameService(new AuthTokenDAO(conn), new GameDAO(conn));
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
        db.returnConnection(conn);
        return new Gson().toJson(result);
    }
}
