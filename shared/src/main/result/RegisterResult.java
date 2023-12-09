package result;

/** Represents the result returned from the register request. */
public class RegisterResult {

    private String message;
    /** The username the user wishes to represent him/her-self in the game. */
    private String username;
    /** The unique authToken assigned to the user after registration was successful. */
    private String authToken;
    private String email;
    private String password;
    private boolean success;

    public RegisterResult() {
    }

    public RegisterResult(String message, String username, String authToken, String email, String password, boolean success) {
        this.message = message;
        this.username = username;
        this.authToken = authToken;
        this.email = email;
        this.password = password;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
