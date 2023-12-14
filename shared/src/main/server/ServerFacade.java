package server;

import com.google.gson.Gson;

import exception.ResponseException;
import request.*;
import result.*;

import java.io.*;
import java.net.*;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public ClearResult clear() throws Exception {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, ClearResult.class);
    }

    public RegisterResult registerUser(RegisterRequest request) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest request) throws Exception {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class);
    }

    // ------------------------- GAME FUNCTIONALITY ------------------------------

    public CreateGameResult createGame(CreateGameRequest request) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws Exception {
        var path = "/game";
        if (request.getAuthToken() != null) {
            SessionManager.setAuthToken(request.getAuthToken());
        } else {
            SessionManager.setAuthToken(null);
        }
        return this.makeRequest("PUT", path, request, JoinGameResult.class);
    }

    public ListGameResult listGames(ListGamesRequest request) throws Exception {
        var path = "/game";
        if (request.getAuthToken() != null) {
            SessionManager.setAuthToken(request.getAuthToken());
        } else {
            SessionManager.setAuthToken(null);
        }
        return this.makeRequest("GET", path, null, ListGameResult.class);
    }

    // ----------------------------------- END -----------------------------------

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = new URI(serverUrl + path).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            // Setting authToken for a session
            if (SessionManager.getAuthToken() != null) {
                http.addRequestProperty("Authorization", SessionManager.getAuthToken());
            }

            http.connect();

            // Checks if the request body is empty or not
            if (request != null) {
                writeBody(request, http);
            }

            return readBody(http, responseClass);

        } catch (Exception e) {
            throw new ResponseException("Server request failed", e);
        }
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;

        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }

                // Get the authToken from login
                if (response instanceof LoginResult) {
                    // Get the authToken
                    String authToken = ((LoginResult) response).getAuthToken();
                    SessionManager.setAuthToken(authToken);
                } else if (response instanceof RegisterResult) {
                    String authToken = ((RegisterResult) response).getAuthToken();
                    SessionManager.setAuthToken(authToken);
                }

                // Retrieve the game??
                // get game ID, then chessGame which has board.
                if (response instanceof CreateGameResult) {
                    Integer gameID = ((CreateGameResult) response).getGameID();
                    SessionManager.setGameID(gameID);
                }

                // Retrieve needed info to display the list of games
                if (response instanceof ListGameResult gameResult) {
                    List<ListGameSuccessResult> games = gameResult.getGames();
                    SessionManager.setGames(games);
                }
            }
        }
        return response;
    }
}
