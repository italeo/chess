package result;

/** Represents the result from the login request, by the user.*/
public class LoginResult {
    /** The user's username in the game. */
    private final String username;
    /** The user's authToken assigned to it, after logging in. */
    private final String authToken;

    /** Constructs the login results from the request.
     * @param username  - The username of the player/user.
     * @param authToken - The unique token create for the user to access specific functionalities from the game. */
    public LoginResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
