package webSeverMessages.serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {
    private String game;

    public LoadGame(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String getGame() {
        return game;
    }
}
