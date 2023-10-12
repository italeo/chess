package request;

/** Represents the request of joining a game. */
public class JoinGameRequest {
    private final String gameID;
    private final String playerColor;
    private final String AuthToken;

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
}
