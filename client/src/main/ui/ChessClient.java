package ui;

import exception.ResponseException;
import model.*;
import request.*;
import result.*;
import server.ServerFacade;
import server.SessionManager;
import java.util.Arrays;
import java.util.List;


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

            // if the command is "quit" and the user is logged in, leave the game.
            if (cmd.equals("quit") && state == State.LOGGED_IN) {
                return quitGame();
            }


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

    private String quitGame() {
        // Switch out of game
        state = State.LOGGED_IN;

        return "Successfully quited the game.\n";
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

    // ----------------------------- GAME FUNCTION --------------------------------------------------------------
    private String createGame(String[] params) {
        if (params.length == 1) {
            // Get the gameName entered by the user
            String gameName = params[0];

            try {
                // Set that name as the gameName
                CreateGameResult result = facade.createGame(gameName);

                // Check if the game was create correctly
                if(result.isSuccess()) {
                    Integer gameID = SessionManager.getGameID();
                    String message = "game created successfully!\n";
                    return message + "you can now join game: " + gameName + "\nwith gameID: " + gameID + "\n";
                }

            } catch (ResponseException e) {
                return e.getMessage();
            }
        }
        return null;
    }

    private String listGames() {
        try {
            ListGameResult result = facade.listGames();

            if (result.isSuccess()) {
                List<ListGameSuccessResult> games = SessionManager.getGames();

                StringBuilder sb = new StringBuilder("Here are the available games!\n");

                for (ListGameSuccessResult game : games) {
                    sb.append("gameID: ").append(game.getGameID()).append(", name: ").append(game.getGameName()).append("\n");
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
            int gameID = Integer.parseInt(gameIDStr);

            try {
                JoinGameResult result = facade.joinGame(gameID, null);

                if (result.isSuccess()) {
                    // print the board here
                    System.out.println("Before board\n");
                    ChessBoardDrawer boardDrawer = new ChessBoardDrawer();
                    boardDrawer.drawBoard(gameID);
                    System.out.flush();
                    Thread.sleep(100);
                    System.out.println("After board\n");
                    return "Successfully Joined as an observer!\n";
                }

            } catch (ResponseException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return "Entered wrong inputs, please try again";
    }

    private String joinGame(String[] params) {
        if (params.length == 2) {
            String gameIDStr = params[0];
            int gameID = Integer.parseInt(gameIDStr);
            String playerColor = params[1];

            try {
                JoinGameResult result = facade.joinGame(gameID, playerColor.toUpperCase());

                if (result.isSuccess()) {
                    // print the board here
                    System.out.println("Before board\n");
                    ChessBoardDrawer boardDrawer = new ChessBoardDrawer();
                    boardDrawer.drawBoard(gameID);
                    System.out.flush();
                    Thread.sleep(100);
                    System.out.println("After board\n");
                    return "Successfully Joined game: " + gameID;
                }
            } catch (ResponseException | InterruptedException e) {
                throw new RuntimeException(e);
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
