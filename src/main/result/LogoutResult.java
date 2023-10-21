package result;

/** The result object responsible for user logout. */
public class LogoutResult {
    private final String message;

    public LogoutResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
