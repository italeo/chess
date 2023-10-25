package result;

/** Represents the result of creating a new chess game. */
public class CreateGameResult {
   private Integer gameID;
   private String message;
   private String gameName;

    public CreateGameResult() {
    }

    /** Constructs new game from the request.

     * @param message - Returns a message informing the user if the game was created or not.
     * */



    public CreateGameResult(Integer gameID, String message, String gameName) {
        this.gameID = gameID;
        this.message = message;
        this.gameName = gameName;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
