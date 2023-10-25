package request;

/** Represents the request for listing all available chess games.
 * */

public class ListGamesRequest {

    private String authToken;
    public ListGamesRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
