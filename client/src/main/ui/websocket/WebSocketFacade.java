package ui.websocket;

import com.google.gson.Gson;
import webSeverMessages.serverMessages.Notification;
import webSeverMessages.userCommands.JoinObserver;
import webSeverMessages.userCommands.JoinPlayer;
import webSeverMessages.userCommands.Leave;
import webSeverMessages.userCommands.Resign;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    // Session variable used to establish game play
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // Set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    switch (notification.getServerMessageType()) {
                        case ERROR -> errorNotification(notification);
                        case LOAD_GAME -> loadGameNotification(notification);
                        case NOTIFICATION -> generalNotification(notification);
                    }
                    //notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | IOException | DeploymentException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Methods to display the Notifications on to the screen --------------------------
    private void errorNotification(Notification notification) {
        String errorMsg = notification.getMessage();
        System.out.print(errorMsg);
    }

    private void loadGameNotification(Notification notification) {
        String loadGameMsg = notification.getMessage();
        System.out.print(loadGameMsg);
    }

    private void generalNotification(Notification notification) {
        String notificationMsg = notification.getMessage();
        System.out.print(notificationMsg);
    }

    // Required method for EndPoint
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    // ---------------------- Methods needed to connect to WebSocketHandler -----------------------------------

    public void joinObserver(JoinObserver joinObserver) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(joinObserver));
    }

    public void joinPlayer(JoinPlayer joinPlayer) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
    }

    public void leaveGame(Leave leave) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(leave));
    }

    public void resignPlayer(Resign resign) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(resign));
    }


    // ------------------------------------------- END --------------------------------------------------------
}
