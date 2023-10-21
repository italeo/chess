package result;


import com.google.gson.Gson;

/** This class is responsible to handle the clearing of the results that we get from the web API calls. */
public class ClearResult {
    /** Message displayed for when clearing is successful */
    String message;
    /** Boolean to indicate if clearing was successful or not. */
    boolean success;

    public ClearResult() {
        this.message = new String();
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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
