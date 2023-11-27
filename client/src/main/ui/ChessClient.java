package ui;

import exception.ResponseException;
import model.Game;
import request.*;
import result.*;
import server.ServerFacade;
import server.SessionManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ChessClient {
    public final String serverUrl;
    private final ServerFacade facade;
    private State state = State.LOGGED_OUT;
    private HashMap<Integer, Integer> indexer;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.facade = new ServerFacade(serverUrl);
        indexer = new HashMap<>();
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
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observer" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    private String clear() {
        try {
            ClearResult result = facade.clear();
            if (result.isSuccess()) {
                return "Clear Successful!";
            }
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest newUser = new RegisterRequest(username, password, email);
            try {
                RegisterResult result = facade.registerUser(newUser);
                // Switch the state to login if it works.
                if (result.isSuccess()) {
                    String newUsername = result.getUsername();
                    state = State.LOGGED_IN;
                    return String.format("Logged in as %s", newUsername);
                } else {
                    return result.getMessage();
                }
            } catch (ResponseException e) {
                throw new ResponseException(403, "Error: already taken\n");
            }
        }
        return "Oops! Looks like you missed something while registering.\n";
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

    // ----------------------------- GAME FUNCTION --------------------------------------------------------------
    private String createGame(String[] params) throws ResponseException {
        if (params.length == 1) {
            // Get the gameName entered by the user
            String gameName = params[0];

            // Set that name as the gameName
            CreateGameRequest request = new CreateGameRequest(SessionManager.getAuthToken(), gameName);
            CreateGameResult result = facade.createGame(request);

            // Check if the game was create correctly
            if(result.isSuccess()) {
                Integer gameID = SessionManager.getGameID();
                return "game created successfully!\n";
            }

        }
        return null;
    }

    private String listGames() {
        try {
            ListGamesRequest request = new ListGamesRequest(SessionManager.getAuthToken());
            ListGameResult result = facade.listGames(request);
            // Tracker needed to map the game index to the gameID
            int index = 1;

            if (result.isSuccess()) {
                List<ListGameSuccessResult> games = SessionManager.getGames();

                StringBuilder sb = new StringBuilder("Here are the available games!\n");

                for (ListGameSuccessResult game : games) {

                    sb.append("Game index: " + index).append(" gameID: ").append(game.getGameID()).append(", name: ").append(game.getGameName()).append("\n");

                    // indexer
                    indexer.put(index++, game.getGameID());
                }
                return sb.toString();
            } else {
                return "There are no games available.";
            }
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private String observeGame(String[] params) {
        if (params.length == 1) {
            String gameIDStr = params[0];
            int index = indexer.get(Integer.parseInt(gameIDStr));

            try {
                JoinGameRequest request = new JoinGameRequest(SessionManager.getAuthToken(), null, index);
                JoinGameResult result = facade.joinGame(request);

                if (result.isSuccess()) {
                    // print the board here
                    ChessBoardDrawer boardDrawer = new ChessBoardDrawer();
                    boardDrawer.drawBoard(index, null);
                    System.out.flush();
                    return "\nSuccessfully Joined as an observer!\n";
                }

            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
        }
        return "Entered wrong inputs, please try again";
    }

    private String joinGame(String[] params) {
        if (params.length == 2) {
            String gameIDStr = params[0];
            int index = indexer.get(Integer.parseInt(gameIDStr));
            String playerColor = params[1].toUpperCase();

            try {
                JoinGameRequest request = new JoinGameRequest(SessionManager.getAuthToken(), playerColor, index);
                JoinGameResult result = facade.joinGame(request);

                if (result.isSuccess()) {
                    // print the board here
                    ChessBoardDrawer boardDrawer = new ChessBoardDrawer();
                    boardDrawer.drawBoard(index, playerColor.toUpperCase());
                    System.out.flush();
                    return "Successfully Joined game: " + index;
                } else {
                    System.out.print("Error joining the game");
                    System.out.println(result.getMessage());
                }
            } catch (ResponseException e) {
                System.out.println("In the catch clause");
                e.printStackTrace();
            }
        }
        return "Entered wrong inputs, please try again";
    }


    // ----------------------------------------- END ------------------------------------------------------------

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    clear - clear the data base
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
                clear - clear the data base
                help - with possible commands
                """;
    }

    public State getState() {
        return state;
    }
}
