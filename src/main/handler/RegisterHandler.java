package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
/** Handler responsible to handle the http request for a new user to register. */
public class RegisterHandler implements HttpHandler {
    /** This function handles the actual http request from the user to register as a user.
     * @param exchange - The http request that contains username, password and email to register as a user.
     * may also contain other necessary information.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
