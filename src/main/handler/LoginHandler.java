package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
/** The handler responsible to handle the http log in request from the user.*/
public class LoginHandler implements HttpHandler {
    /**This function handles the actual request
     * @param exchange - The http request containing the username and password along with
     * other possible information needed for the user to log in.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
