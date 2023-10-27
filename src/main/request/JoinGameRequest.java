package request;

/** Represents the request of joining a game. */
public class JoinGameRequest {
    private final String playerColor;
    private final int gameID;
    private String authToken;

    // Initializes the request with the needed information to join the game, which is the authToken, the player color
    // they are joining as and the gameID
    public JoinGameRequest(String authToken, String playerColor, int gameID) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }
}
