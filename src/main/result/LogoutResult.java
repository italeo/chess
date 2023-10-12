package result;

/** The result object responsible for user logout. */
public class LogoutResult {
    /** Used to help indicate if the user has logged out successfully. */
    private final boolean success;
    /** Message to inform the user, that they have logged out successfully. */
    private final String message;

    /** Constructs the result from the logout request.
     * @param success - true if the user logs out successfully.
     * @param message - A message informing the user if logout was successful or not.
     * */
    public LogoutResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
