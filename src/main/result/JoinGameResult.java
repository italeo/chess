package result;

/** This class is responsible for the result of the join game request. */
public class JoinGameResult {
    private String message;

    public JoinGameResult() {
    }

    public JoinGameResult(String message) {
        this.message = message;
    }

    public void setGame(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
