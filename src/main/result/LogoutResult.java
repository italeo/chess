package result;

/** The result object responsible for user logout. */
public class LogoutResult {
    private String message;
    private boolean success;

    public LogoutResult() {
    }

    public LogoutResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
