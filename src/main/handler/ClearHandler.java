package handler;

import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.GameDAO;
import dao.UserDAO;
import result.ClearResult;
import service.ClearService;
import spark.*;

/** The Http handler for clearing request. */
public class ClearHandler {

    public Object handle(Response result) {
        ClearService service = new ClearService(new AuthTokenDAO(), new GameDAO(), new UserDAO());
        ClearResult clearResult = service.clear();
        result.type("application/json");

        if (clearResult.getMessage() == null) {
            result.status(200);
        } else {
            result.status(500);
        }

        return new Gson().toJson(clearResult);
    }
}
