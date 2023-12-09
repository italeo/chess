package webSeverMessages.userCommands;

import webSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {
    public Integer gameID;

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }

    public Integer getGameID() {
        return gameID;
    }
}
