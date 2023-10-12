package request;

/**
 * Represents the logout request from the user. */
public class LogoutRequest {
    private String authToken;
    /** Constructs the logout request from the user.
     * @param authToken - The unique token that allows the user to logout.
     * */
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
