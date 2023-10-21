package result;

import chess.ChessGame;

/** Represents the result of creating a new chess game. */
public class CreateGameResult {
   private int gameID;
   private String message;

    /** Constructs new game from the request.
     * @param success - The boolean will return true if the game was created correctly according to the rules that were defined.
     * @param message - Returns a message informing the user if the game was created or not.
     * @param createdGame - The new game object create.
     * */

    public CreateGameResult(int gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }
}
