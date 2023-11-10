package ui;

import ui.websocket.NotificationHandler;
import javax.management.Notification;

public class Repl implements NotificationHandler {
    // Needed to call the client that handles all the functionality of the game
    private final ChessClient client;

    public Repl(String serverUrl) {
        this.client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("👑 Welcome to 240 chess. Type Help to get started. 👑");
        System.out.print(client.help());
    }


    @Override
    public void notify(Notification notification) {

    }
}
