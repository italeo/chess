package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
/** Handler for http request from user to logout. */
public class LogoutHandler implements HttpHandler {
    /** Function that handles the actual http request from the user to logout of the game.
     * @param exchange - the http request containing the users authToken and other possible information.
     * @throws IOException when an I/O error occurs.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
