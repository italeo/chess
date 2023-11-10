package ui;

import ui.websocket.NotificationHandler;

public class ChessClient {
    public final String serverUrl;
    private final NotificationHandler notificationHandler;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
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
