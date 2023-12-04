package webSeverMessages.userCommands;

public class Resign {
    private Integer gameID;

    public Resign(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
