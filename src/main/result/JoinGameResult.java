package result;

/** This class is responsible for the result of the join game request. */
public class JoinGameResult {
    private String message;

    public JoinGameResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
