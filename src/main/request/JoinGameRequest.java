package request;

/** Represents the request of joining a game. */
public class JoinGameRequest {
    /** The game's identifier. */
    private String gameID;
    /** The player's color, it will either be black or white representing the color of the chess pieces. */
    private String playerColor;
    /** The token assigned to the user during log in or registration. */
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
