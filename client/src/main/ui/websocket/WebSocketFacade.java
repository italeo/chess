package ui.websocket;

import com.google.gson.Gson;
import model.User;
import webSeverMessages.serverMessages.Notification;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    // Session variable used to establish game play
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // Set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | IOException | DeploymentException e) {
            e.printStackTrace();
        }
    }

    // Required method for EndPoint
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    // ---------------------- Methods needed to connect to WebSocketHandler -----------------------------------

    // Might not need redraw call
    public void redraw() {

    }

    public void leaveGame(String authToken) {
        var userCmd = new UserGameCommand(authToken);
    }

    public void make() {

    }

    public void resign() {

    }

    // ------------------------------------------- END --------------------------------------------------------
}
