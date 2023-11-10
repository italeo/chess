package ui;

import ui.websocket.NotificationHandler;
import javax.management.Notification;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.State.*;
public class Repl implements NotificationHandler {
    // Needed to call the client that handles all the functionality of the game
    private final ChessClient client;

    public Repl(String serverUrl) {
        this.client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("👑 Welcome to 240 chess. Type Help to get started. 👑");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

//            try {
//                result = client.eval(line);
//            } catch ()
        }
    }


    @Override
    public void notify(Notification notification) {
    }

    private void printPrompt() {
        System.out.print("\n" +  SET_TEXT_BOLD + ">>>" + SET_TEXT_COLOR_GREEN);
    }
}
