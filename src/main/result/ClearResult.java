package result;


/** This class is responsible to handle the clearing of the results that we get from the web API calls. */
public class ClearResult {

    String message;
    boolean success;

    /** The constructor for the ClearResult object responsible to clear all the requested results.
     * @param message - Provided message informing the user if the clear function had performed correctly or not
     * @param success - Boolean showing if the clear function performed accordingly */
    public ClearResult(String message, boolean success) {
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
