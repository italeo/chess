package ui;

import chess.ChessGame;
import chess.ChessMoveImpl;
import chess.ChessPositionImpl;
import request.*;
import result.*;
import server.ServerFacade;
import server.SessionManager;
import ui.websocket.WebSocketFacade;
import webSeverMessages.userCommands.*;

import java.util.*;


public class ChessClient {
    public final String serverUrl;
    private final ServerFacade facade;
    private State state = State.LOGGED_OUT;
    private final HashMap<Integer, Integer> indexer;
    private final WebSocketFacade webSocketFacade;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.facade = new ServerFacade(serverUrl);
        indexer = new HashMap<>();
        this.webSocketFacade = new WebSocketFacade(serverUrl);
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
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> (state == State.GAME_PLAY) ? gamePlayCommands(cmd, params) : help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String gamePlayCommands(String cmd, String[] params) throws Exception {
        return switch (cmd) {
            case "redraw" -> redraw();
            case "leave" -> leaveGame();
            case "make" -> makeMove(params);
            case "resign" -> resign();
            case "highlight" -> highlight(params);
            default -> "Invalid command for game play state, type 'help' for list of valid commands.";
        };
    }

    private String clear() {
        try {
            ClearResult result = facade.clear();
            if (result.isSuccess()) {
                return "Clear Successful!";
            }
        } catch (Exception e) {
            return "Error: clear failed";
        }
        return "";
    }

    private String register(String... params) {
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
            } catch (Exception e) {
                return "Error: Register failed\n";
            }
        }
        return "Looks like you missed a few things while registering. Please try again\n";
    }

    private String login(String[] params) {
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

            } catch (Exception e) {
                return "Oops! Looks like this user isn't registered.\n";
            }
        }
        return "Sorry you entered the wrong information, try again";
    }

    private String logout() {

        try {
            LogoutRequest request = new LogoutRequest(SessionManager.getAuthToken());
            // Call logout method from Facade
            LogoutResult result = facade.logout(request);
            if (result.isSuccess()) {
                // Update the state
                state = State.LOGGED_OUT;
                // Return the success message
                return "Logout successful";
            } else {
                return " Log out failed: ";
            }
        } catch (Exception e) {
            return "Error: logout fail\n";
        }
    }

    // ----------------------------- GAME FUNCTION --------------------------------------------------------------
    private String createGame(String[] params) throws Exception {
        if (params.length == 1) {
            // Get the gameName entered by the user
            String gameName = params[0];

            // Set that name as the gameName
            CreateGameRequest request = new CreateGameRequest(SessionManager.getAuthToken(), gameName);
            CreateGameResult result = facade.createGame(request);

            // Check if the game was create correctly
            if (result.isSuccess()) {
                return "game created successfully!\n";
            }
        } else {
            return "Please enter the name of your game.";
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

                    sb.append("Game index: ").append(index).append(" gameID: ")
                            .append(game.getGameID())
                            .append(", Name: ")
                            .append(game.getGameName())
                            .append(", WhiteUser: ")
                            .append(game.getWhiteUsername())
                            .append(", BlackUser: ")
                            .append(game.getBlackUsername())
                            .append("\n");

                    // indexer
                    indexer.put(index++, game.getGameID());
                }
                return sb.toString();
            } else {
                return "Create a game!";
            }
        } catch (Exception e) {
            return "Error: listGames failed\n";
        }
    }

    // ---------------------------------- USES WEB SOCKETS -------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    private String observeGame(String[] params) {
        if (params.length == 1) {
            String gameIDStr = params[0];
            int gameID = indexer.get(Integer.parseInt(gameIDStr));
            String authToken = SessionManager.getAuthToken();

            try {
                // Change state into GAME_PLAY
                state = State.GAME_PLAY;

                JoinGameRequest request = new JoinGameRequest(SessionManager.getAuthToken(), null, gameID);
                JoinGameResult result = facade.joinGame(request);

                if (result.isSuccess()) {

                    // Send the UserCommand for joinObserver
                    JoinObserver joinObserver = new JoinObserver(authToken, gameID);
                    webSocketFacade.joinObserver(joinObserver);

                    return "\nSuccessfully Joined as an observer!\n";
                }

            } catch (Exception e) {
                return "Error: observe Game\n";
            }
        }
        return "Entered wrong inputs, please try again";
    }

    private String joinGame(String[] params) {
        if (params.length == 2) {
            String gameIDStr = params[0];
            int gameID = indexer.get(Integer.parseInt(gameIDStr));
            String playerColor = params[1].toUpperCase();

            try {
                // Change state into GAME_PLAY
                state = State.GAME_PLAY;

                JoinGameRequest request = new JoinGameRequest(SessionManager.getAuthToken(), playerColor, gameID);
                JoinGameResult result = facade.joinGame(request);

                if (result.isSuccess()) {

                    // Info for the JoinPlayer
                    String authToken = SessionManager.getAuthToken();
                    ChessGame.TeamColor teamColor;


                    if (playerColor.equals("WHITE")) {
                        teamColor = ChessGame.TeamColor.WHITE;

                    } else {
                        teamColor = ChessGame.TeamColor.BLACK;
                    }

                    // Send the UserCommand for joinPlayer
                    JoinPlayer joinPlayer = new JoinPlayer(authToken, gameID, teamColor);
                    webSocketFacade.joinPlayer(joinPlayer);

                    return "Successfully Joined game: " + gameID + " as the " + playerColor.toUpperCase() + " player\n";
                } else {
                    System.out.print("Error joining the game");
                    System.out.println(result.getMessage());
                }
            } catch (Exception e) {
                state = State.LOGGED_IN;
                return String.format("Looks likes %s is already taken", playerColor);
            }
        }
        return "Entered wrong inputs, please try again";
    }

    private String redraw() {
        return "";
    }

    private String leaveGame() throws Exception {
        String authToken = SessionManager.getAuthToken();
        Integer gameID = SessionManager.getGameID();

        // Create Leave userCommand
        Leave leave = new Leave(authToken, gameID);
        webSocketFacade.leaveGame(leave);

        state = State.LOGGED_IN;

        return String.format("Successfully left game: %s", gameID);
    }


    private String makeMove(String[] params) throws Exception {
        // Info needed to make a move
        String authToken = SessionManager.getAuthToken();
        int gameID = SessionManager.getGameID();

        if (params.length == 1) {
            String move = params[0];
            if (move.length() == 4) {
                String rowB = String.valueOf(move.charAt(0));
                String colB = String.valueOf(move.charAt(1));
                String rowT = String.valueOf(move.charAt(2));
                String colT = String.valueOf(move.charAt(3));

                // Since the original board in my chess implementation is flipped I'll need to reset the positions

                // Resetting the rows
                String rowBStr = convertRowInt(rowB);
                String rowTStr = convertRowInt(rowT);

                // Resetting the columns
                String colBStr = convertColumnToInt(colB);
                String colTStr = convertColumnToInt(colT);

                // Convert the string positions to ints
                // Starting positions
                int rowBInt = Integer.parseInt(rowBStr);
                int colBInt = Integer.parseInt(colBStr);

                // End positions
                int rowTInt = Integer.parseInt(rowTStr);
                int colTInt = Integer.parseInt(colTStr);

                // Get the Piece in that position
                ChessPositionImpl startPosition = new ChessPositionImpl(rowBInt, colBInt);
                ChessPositionImpl endPosition = new ChessPositionImpl(rowTInt, colTInt);

                //
                ChessMoveImpl chessMove = new ChessMoveImpl(startPosition, endPosition, null);

                // Send the move to the server
                MakeMove makeMove = new MakeMove(authToken, gameID, chessMove);
                webSocketFacade.makeMove(makeMove);
            }
        }

        return String.format("move: %s, made", params[0]);
    }

    private String convertColumnToInt(String str) {
        if (Objects.equals(str, "a")) {
            return "7";
        }
        if (Objects.equals(str, "b")) {
            return "6";
        }
        if (Objects.equals(str, "c")) {
            return "5";
        }
        if (Objects.equals(str, "d")) {
            return "4";
        }
        if (Objects.equals(str, "e")) {
            return "3";
        }
        if (Objects.equals(str, "f")) {
            return "2";
        }
        if (Objects.equals(str, "g")) {
            return "1";
        }
        if (Objects.equals(str, "h")) {
            return "0";
        }

        return "Sorry invalid input for rows";
    }

    private String convertRowInt(String str) {
        if (Objects.equals(str, "1")) {
            return "7";
        }
        if (Objects.equals(str, "2")) {
            return "6";
        }
        if (Objects.equals(str, "3")) {
            return "5";
        }
        if (Objects.equals(str, "4")) {
            return "4";
        }
        if (Objects.equals(str, "5")) {
            return "3";
        }
        if (Objects.equals(str, "6")) {
            return "2";
        }
        if (Objects.equals(str, "7")) {
            return "1";
        }
        if (Objects.equals(str, "8")) {
            return "0";
        }
        return "Sorry invalid input for columns";
    }


    private String highlight(String[] params) {
        return "";
    }

    private String resign() throws Exception {
        String authToken = SessionManager.getAuthToken();
        Integer gameID = SessionManager.getGameID();

        // Create resign userCommand
        Resign resign = new Resign(authToken, gameID);
        webSocketFacade.resignPlayer(resign);

        // Making sure whoever is resigning leaves the Game Play state
        state = State.LOGGED_IN;

        return String.format("Successfully resigned from game: %s", gameID);
    }

    // ----------------------------------------- END OF WEBSOCKET FUNCTIONS -------------------------------------------
    // ----------------------------------------------------------------------------------------------------------------

    // ----------------------------------------- END OF HTTP FUNCTIONS -------------------------------------------------

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        } else if (state == State.LOGGED_IN) {
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK|<empty>]- a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    clear - clear the data base
                    help - with possible commands
                    """;
        } else {
            return """
                    redraw - Redraws the chess board upon the user’s request.
                    leave - Removes the user from the game
                    make <move> - make a move in the game when it is your turn (only for players)
                    highlight <piece> - Highlights the valid move for a piece (specify with letters. e.g. K = King)
                    resign - Allows you to resign the game, if you do you will forfeit the game
                    help - with possible commands
                    """;
        }
    }

    public State getState() {
        return state;
    }
}
