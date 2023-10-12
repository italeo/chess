package service;

/** Responsible to allow players to join a game if the game is available. */
public class JoinGameService {
    /** Allows player to join a specific game using the game ID.
     * @param gameID - String the represents the specific gameID for a particular game.
     * @param playerName - The name of the player trying to join the game
     * */
    public boolean joinGame(String gameID, String playerName) {
        return false;
    }

    /** Checks if the game is available or not.
     * @param gameID - The code to get into that specific game
     * */
    public boolean isGameAvailable(String gameID) {
        return false;
    }

}
