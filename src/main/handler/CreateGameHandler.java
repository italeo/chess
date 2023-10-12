package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/** Responsible to handle the  http CreateGameRequest.
 * */
public class CreateGameHandler implements HttpHandler {

    /** Handles the http request to create a new chess game with the default settings stated in the rules.
     * @param exchange - The http request containing the players names and other required information.
     * @throws IOException if an I/O error occurs.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
