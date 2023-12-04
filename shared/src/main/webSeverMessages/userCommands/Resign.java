package webSeverMessages.userCommands;

import webSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {
    private Integer gameID;

    public Resign(Integer gameID) {
        super(String.valueOf(CommandType.RESIGN));
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
