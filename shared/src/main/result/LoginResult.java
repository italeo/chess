package result;

/** Represents the result from the login request, by the user.*/
public class LoginResult {
    private String authToken;
    private String message;
    private String username;
    private boolean success;

    public LoginResult() {
    }

    public LoginResult(String authToken, String message, String username, boolean success) {
        this.authToken = authToken;
        this.message = message;
        this.username = username;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
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
