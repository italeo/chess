package result;

/** Represents the result of the list of all possible games the player could join. */
public class ListGameResult {
    private final int[] gameIDs;
    private final String[] whiteUsernames;
    private final String[] blackUsernames;
    private final String[] gameNames;

    /** The result returned from the list game request specified by these parameters.
     * @param gameIDs - The unique ID given to each game so that the player can access them.
     * @param whiteUsernames - The usernames for each white player in the game.
     * @param blackUsernames - The usernames for each black player in the game.
     * @param gameNames - An array of names for all the games available.
     * */
    public ListGameResult(int[] gameIDs, String[] whiteUsernames, String[] blackUsernames, String[] gameNames) {
        this.gameIDs = gameIDs;
        this.whiteUsernames = whiteUsernames;
        this.blackUsernames = blackUsernames;
        this.gameNames = gameNames;
    }

    public int[] getGameIDs() {
        return gameIDs;
    }

    public String[] getWhiteUsernames() {
        return whiteUsernames;
    }

    public String[] getBlackUsernames() {
        return blackUsernames;
    }

    public String[] getGameNames() {
        return gameNames;
    }
}
