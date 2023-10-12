package result;

/** This class is responsible for the result of the join game request. */
public class JoinGameResult {
    private final boolean success;
    private final String message;
    private final int gameID;


    /** The constructor is responsible to collect the requested info which is specified by these parameters.
     * @param success - Returns true or false if the player successfully joins the game.
     * @param message - A message informing the user if they joined the game successfully or not.
     * @param gameID - The unique identifier that specifies which game the user/player is joining.
     * */
    public JoinGameResult(boolean success, String message, int gameID) {
        this.success = success;
        this.message = message;
        this.gameID = gameID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getGameID() {
        return gameID;
    }
}
