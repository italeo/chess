package ui;

import exception.ResponseException;
import model.User;
import request.LoginRequest;
import server.ServerFacade;
import java.util.Arrays;

public class ChessClient {
    public final String serverUrl;
    private final ServerFacade facade;
    private State state = State.LOGGED_OUT;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.facade = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> {
                    String result = register(params);
                    yield result;
                }
                case "login" -> {
                    String result = login(params);
                    yield result;
                }
                case "quit" -> "quit";
                case "logout" -> logout(params);
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private String logout(String[] params) throws ResponseException {
        try {
            // Call logout method from Facade
            facade.logout();

            // Update the state
            state = State.LOGGED_OUT;

            // Return the success message
            return " Logged out successfully";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            User newUser = new User(params[0], params[1], params[2]);
            try {
                User registeredUser = facade.registerUser(newUser);
                state = State.LOGGED_IN;
                return String.format("Logged in as %s", registeredUser.getUsername());
            } catch (ResponseException e) {
                throw new ResponseException(400, "Expected: <yourname>");
            }
        }
        return "Invalid number of parameters";
    }

    private String login(String[] params) throws ResponseException {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];

            // Create a loginRequest object with the username and password
            LoginRequest loginRequest = new LoginRequest(username, password);
            try {
                // Call the login method from the SeverFacade
                facade.login(loginRequest);

                // Update state to logged in
                state = State.LOGGED_IN;

                // Return success message
                return String.format("Logged in as %s", username);

            } catch (ResponseException e) {
                throw new ResponseException(400, e.getMessage());
            }
        }
        return "Sorry you entered the wrong information, try again";
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<empty>]- a game
                observer <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    public State getState() {
        return state;
    }
}
