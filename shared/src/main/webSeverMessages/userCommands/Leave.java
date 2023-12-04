package webSeverMessages.userCommands;

import webSocketMessages.userCommands.UserGameCommand;

public class Leave extends UserGameCommand {
    private Integer gameID;

    public Leave(Integer gameID) {
        super(String.valueOf(CommandType.LEAVE));
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
