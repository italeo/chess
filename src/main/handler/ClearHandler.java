package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import result.ClearResult;
import service.ClearService;
import spark.*;

/** The Http handler for clearing request. */
public class ClearHandler implements Route {

    // Handles the request we get from the client and returns the response from the server, the request is passed to the service
    // where it then returns the results for the clear request.
    public Object handle(Request request, Response response) {
        ClearService service = new ClearService(new GameDAO(), new UserDAO(), new AuthTokenDAO());
        ClearResult result = service.clear();
        response.type("application/json");

        if (result.isSuccess()) {
            response.status(200);
        } else {
            response.status(500);
        }
        return new Gson().toJson(result);
    }
}
