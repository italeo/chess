package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import result.ClearResult;
import service.ClearService;
import spark.*;

import java.sql.Connection;

/** The Http handler for clearing request. */
public class ClearHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the clear request.
    public Object handle(Request request, Response response) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();
        ClearService service = new ClearService(new GameDAO(conn), new UserDAO(conn), new AuthTokenDAO(conn));
        ClearResult result = service.clear();
        response.type("application/json");

        if (result.isSuccess()) {
            response.status(200);
        } else {
            response.status(500);
        }
        db.returnConnection(conn);
        return new Gson().toJson(result);
    }
}
