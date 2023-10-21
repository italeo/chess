package result;

/** Represents the result of the list of all possible games the player could join. */
public class ListGameResult {
    ListGameSuccessResult[] games;

    private String message;

    public ListGameResult(ListGameSuccessResult[] games, String message) {
        this.games = games;
        this.message = message;
    }

    public ListGameSuccessResult[] getGames() {
        return games;
    }

    public String getMessage() {
        return message;
    }
}
