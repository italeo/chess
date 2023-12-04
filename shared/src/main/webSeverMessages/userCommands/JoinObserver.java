package webSeverMessages.userCommands;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand {
    private Integer gameID;

    public JoinObserver(Integer gameID) {
        super(String.valueOf(CommandType.JOIN_OBSERVER));
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
