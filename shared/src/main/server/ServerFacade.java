package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import request.*;
import result.*;
import java.io.*;
import java.net.*;


public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RegisterResult registerUser(User request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class);
    }

    public LogoutResult logout() throws ResponseException {
        var path = "/session";
        LogoutRequest request = new LogoutRequest(SessionManager.getAuthToken());
        return this.makeRequest("DELETE", path, request, LogoutResult.class);
    }

    // ------------------------- GAME FUNCTIONALITY ------------------------------

    public CreateGameResult createGame(String gameName) throws ResponseException {
        var path = "/game";
        var request = new CreateGameRequest(SessionManager.getAuthToken(), gameName);
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }





    // ----------------------------------- END -----------------------------------

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = new URI(serverUrl + path).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            // Setting authToken for a session
            if (SessionManager.getAuthToken() != null) {
                http.addRequestProperty("Authorization", SessionManager.getAuthToken());
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try(OutputStream reqBody = http.getOutputStream()) {
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

                if (response instanceof CreateGameResult) {
                    Integer gameID = ((CreateGameResult) response).getGameID();
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if(!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
