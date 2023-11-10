package ui;

import ui.websocket.NotificationHandler;

public class ChessClient {
    public final String serverUrl;
    private final NotificationHandler notificationHandler;
    private State state = State.LOGGED_OUT;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    // Need to complete!!
    public String eval(String input) {
        return "";
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
