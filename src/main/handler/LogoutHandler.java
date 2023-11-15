package handler;

import com.google.gson.Gson;
import dao.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import request.*;
import result.*;
import service.LogoutService;
import spark.*;

import java.sql.Connection;

/** Handler for http request from user to logout. */
public class LogoutHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the logout request.
    public Object handle(Request request, Response response) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        LogoutRequest logoutRequest = new LogoutRequest(request.headers("Authorization"));
        LogoutService service = new LogoutService(new AuthTokenDAO(conn));
        LogoutResult result = service.logout(logoutRequest);
        response.type("application/json");

        if (result.getMessage() == null) {
            response.status(200);
        }
        else if(result.getMessage().equals("Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }
        db.returnConnection(conn);
        return new Gson().toJson(result);
    }
}
