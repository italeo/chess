package webSeverMessages.userCommands;

public class Leave {
    private Integer gameID;

    public Leave(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
