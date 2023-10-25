package result;

public class ListGameSuccessResult {
    private String whiteUsername;
    private String blackUsername;
    private String game;
    private int gameID;

    public ListGameSuccessResult() {
    }

    public ListGameSuccessResult(String whiteUsername, String blackUsername, String game, int gameID) {
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.game = game;
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGame() {
        return game;
    }

    public int getGameID() {
        return gameID;
    }
}
