package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/** The Http handler for clearing request. */
public class ClearHandler implements HttpHandler {

    /** This function performs the actual handling of the http request.
     * @param exchange - The actual http request.
     * @throws IOException if an I/O error occurs.
     * */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
