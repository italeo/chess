package webSeverMessages.serverMessages;

import model.Game;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {
    private Game game;

    public LoadGame(Game game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
