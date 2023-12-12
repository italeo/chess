package request;

/** Represents the request to create a new game. */
public class CreateGameRequest {
    private final String gameName;
    private String authToken;

    // Makes sure that for the request we get the authToken and the game name
    public CreateGameRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }
}
