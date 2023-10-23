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

    //
    public Object handle(Request request, Response response) {
        ClearService service = new ClearService(new GameDAO(), new UserDAO());
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
