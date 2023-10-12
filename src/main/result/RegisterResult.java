package result;

/** Represents the result returned from the register request. */
public class RegisterResult {
    /** Used to indicate if a new user has registered successfully or not. */
    private final boolean success;
    /** Message displayed if the new user has registered successfully or not. */
    private final String message;
    /** The username the user wishes to represent him/her-self in the game. */
    private final String username;
    /** The unique authToken assigned to the user after registration was successful. */
    private final String authToken;

    /** Constructs the result of the user registering from the request
     * @param success - True if the registration was successful.
     * @param username - The username of the user registering.
     * @param message - A message displayed to show if the registration was successful
     * @param authToken - A unique token given to the new user during registration and log in.
     * */
    public RegisterResult(boolean success, String message, String username, String authToken) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.authToken = authToken;
    }

    public boolean isSuccess() {
        return success;
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
