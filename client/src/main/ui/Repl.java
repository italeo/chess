package ui;

import ui.websocket.NotificationHandler;
import webSeverMessages.serverMessages.Notification;

import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.State.*;

public class Repl implements NotificationHandler {
    // Needed to call the client that handles all the functionality of the game
    private final ChessClient client;

    public Repl(String serverUrl) {
        this.client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("👑 Welcome to 240 chess. Type Help to get started. 👑");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (result != null && !result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }


    public void notify(Notification notification) {
        System.out.print(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    private void printPrompt() {
        State currentState = client.getState();
        String stateString = (currentState == LOGGED_OUT) ? LOGGED_OUT.name() : LOGGED_IN.name();
        System.out.print("\n" + SET_TEXT_BOLD + "[" + stateString + "]" + " >>> ");
    }

}
