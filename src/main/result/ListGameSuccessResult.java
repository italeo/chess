package result;

public class ListGameSuccessResult {
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private int gameID;

    public ListGameSuccessResult() {
    }

    public ListGameSuccessResult(String whiteUsername, String blackUsername, String gameName, int gameID) {
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

    public String getGameName() {
        return gameName;
    }

    public int getGameID() {
        return gameID;
    }
}
