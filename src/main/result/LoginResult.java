package result;

/** Represents the result from the login request, by the user.*/
public class LoginResult {
    private String authToken;
    private String message;
    private String username;

    public LoginResult(String username, String authToken, String message) {
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }
}
