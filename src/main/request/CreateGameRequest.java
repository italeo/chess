package request;

/** Represents the request to create a new game. */
public class CreateGameRequest {

    private String gameName;
    private String authToken;

    public CreateGameRequest(String gameName, String authToken) {
        this.gameName = gameName;
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }
}
