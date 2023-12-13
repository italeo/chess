package ui.websocket;

import webSeverMessages.serverMessages.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
}
