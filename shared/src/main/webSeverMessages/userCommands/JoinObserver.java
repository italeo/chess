package webSeverMessages.userCommands;

public class JoinObserver {
    private Integer gameID;

    public JoinObserver(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
