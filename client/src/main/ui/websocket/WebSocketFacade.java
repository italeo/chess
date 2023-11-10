package ui.websocket;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {
    // Allows us to establish a session for the user
    Session session;
    // Enables us to handle the notification traffic
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws URISyntaxException {
        url = url.replace("http", "ws");
        URI socketURI = new URI(url + "/connect");
        this.notificationHandler = notificationHandler;

    }

    // The Endpoint requires this function but nothing is needed to be done here
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
