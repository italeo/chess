package result;

/** This class is responsible to handle the clearing of the results that we get from the web API calls. */
public class ClearResult {
    /** Message displayed for when clearing is successful */
    private String message;
    /** Boolean to indicate if clearing was successful or not. */
    private boolean success;

    // Constructs the clear result to respond with a success message and a success boolean
    public ClearResult() {
        this.message = "";
        this.success = false;
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
