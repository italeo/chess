package result;

import java.util.List;

/** Represents the result of the list of all possible games the player could join. */
public class ListGameResult {
    private List<ListGameSuccessResult> games;

    private String message;

    public ListGameResult() {
    }

    public ListGameResult(List<ListGameSuccessResult> games, String message) {
        this.games = games;
        this.message = message;
    }

    public List<ListGameSuccessResult> getGames() {
        return games;
    }

    public void setGames(List<ListGameSuccessResult> games) {
        this.games = games;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
