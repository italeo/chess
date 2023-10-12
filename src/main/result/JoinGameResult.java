package result;

/** This class is responsible for the result of the join game request. */
public class JoinGameResult {
    /** Used to indicate if the user has join the game successfully. */
    private final boolean success;
    /** Message to notify user if they have joined the game successfully. */
    private final String message;
    /** Used to identify which game to join specifically */
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
