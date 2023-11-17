package ui;

import dao.GameDAO;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;
import request.*;
import result.*;
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
                case "clear" -> clear();
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private String clear() {
        return "";
    }

    private String createGame(String[] params) {
        if (params.length == 1) {
            // Get the gameName entered by the user
            String gameName = params[0];

            try {
                // Set that name as the gameName
                CreateGameResult result = facade.createGame(gameName);

                // Check if the game was create correctly
                if(result.isSuccess()) {
                    String successMessage = "game created successfully now print board";

                    // Draw the default board
                    ChessBoardDrawer boardDrawer = new ChessBoardDrawer();

                    Game newGame = (Game) result.getGame();
                    boardDrawer.drawBoard(newGame);

                    return successMessage;
                }

            } catch (ResponseException e) {
                return e.getMessage();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            User newUser = new User(params[0], params[1], params[2]);
            try {
                RegisterResult result = facade.registerUser(newUser);
                // Switch the state to login if it works.
                String username = result.getUsername();
                state = State.LOGGED_IN;
                return String.format("Logged in as %s", username);
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

    private String logout() throws ResponseException {

        try {
            // Call logout method from Facade
            LogoutResult result = facade.logout();
            if (result.isSuccess()) {
                // Update the state
                state = State.LOGGED_OUT;
                // Return the success message
                return "Logout successful";
            } else {
                return " Log out failed: ";
            }
        } catch (ResponseException e) {
            return e.getMessage();
        }
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
                clear - clear the data base
                quit - playing chess
                help - with possible commands
                """;
    }

    public State getState() {
        return state;
    }
}
