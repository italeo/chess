package handler.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    // Responsible to handle all web socket connections
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // Parse the incoming JSON message
        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);

        // Handling different command types
        switch (userCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayerCmd(session, userCommand);
            case JOIN_OBSERVER -> observerCmd(session, userCommand);
            case MAKE_MOVE -> makeMoveCmd(session, userCommand);
            case LEAVE -> leaveCmd(session, userCommand);
            case RESIGN -> resignCmd(session, userCommand);
        }
    }

    private void joinPlayerCmd(Session session, UserGameCommand userCommand) {
        // Get the authToken to verify the "root client"
        String authToken = userCommand.getAuthString();

        // Add connection to set of connections in connectionManager

        // Perform the necessary actions


    }

    private void observerCmd(Session session, UserGameCommand userCommand) {
    }

    private void makeMoveCmd(Session session, UserGameCommand userCommand) {
    }

    private void leaveCmd(Session session, UserGameCommand userCommand) {
    }

    private void resignCmd(Session session, UserGameCommand userCommand) {
    }


}
