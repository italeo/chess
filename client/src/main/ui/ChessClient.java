package ui;

import ui.chessBoardDrawer.*;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.net.URISyntaxException;
import java.util.Arrays;

public class ChessClient {
    public final String serverUrl;
    private String usersName = null;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private State state = State.LOGGED_OUT;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> {
                    String result = register(params);
                    chessBoardDrawer.drawChessBoard(true);
                    yield result;
                }
                case "login" -> {
                    String result = login(params);
                    chessBoardDrawer.drawChessBoard(true);
                    yield result;
                }
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String register(String... params) throws URISyntaxException {
        if (params.length == 3) {
            state = State.LOGGED_IN;
            usersName = String.join("-", params);
            ws = new WebSocketFacade(serverUrl, notificationHandler);

        }
        return "Not yet implemented";
    }

    private String login(String[] params) {
        return "Not yet implemented";
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
}
