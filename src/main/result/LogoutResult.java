package result;

/** The result object responsible for user logout. */
public class LogoutResult {
    private String message;

    public LogoutResult() {
    }

    public LogoutResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
