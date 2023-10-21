package result;

/** Represents the result returned from the register request. */
public class RegisterResult {

    private final String message;
    /** The username the user wishes to represent him/her-self in the game. */
    private final String username;
    /** The unique authToken assigned to the user after registration was successful. */
    private final String authToken;

    public RegisterResult(String message, String username, String authToken) {
        this.message = message;
        this.username = username;
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
}
