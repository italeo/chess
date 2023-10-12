package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/** Handles the http request for listing all the games available*/
public class ListGameHandler implements HttpHandler {

    /** This is the function that handles the actual request specified by this parameter:
     * @param exchange - The http request containing the authToken and required information so that list of games can be returned.
     * @throws IOException - Will be thrown when there is an I/O error.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
