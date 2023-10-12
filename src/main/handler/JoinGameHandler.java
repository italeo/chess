package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/** Responsible for handling the http request from a user to join a game. */
public class JoinGameHandler implements HttpHandler {

    /** Handles the request from a player to join a specific game given the gameID
     * @param exchange - The http request containing the gameID and other required information.
     * @throws IOException - Is thrown when an I/O error occurs.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }
}
