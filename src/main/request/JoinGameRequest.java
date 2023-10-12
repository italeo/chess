package request;

/** Represents the request of joining a game. */
public class JoinGameRequest {
    private String gameID;
    private String playerColor;
    private String AuthToken;

    /** constructs a new join game request with these specific parameters.
     * @param gameID - represents the specific game the player is trying to join.
     * @param playerColor - The color the player is trying to join as, which is either black or white.
     * @param authToken - Specific authentication for authorization.
     * */
    public JoinGameRequest(String gameID, String playerColor, String authToken) {
        this.gameID = gameID;
        this.playerColor = playerColor;
        AuthToken = authToken;
    }

    public String getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }
}
