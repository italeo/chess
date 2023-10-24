package result;

import chess.ChessGame;

/** Represents the result of creating a new chess game. */
public class CreateGameResult {
   private int gameID;
   private String message;
   private String gameName;

    public CreateGameResult() {
    }

    /** Constructs new game from the request.
     * @param success - The boolean will return true if the game was created correctly according to the rules that were defined.
     * @param message - Returns a message informing the user if the game was created or not.
     * @param createdGame - The new game object create.
     * */



    public CreateGameResult(int gameID, String message, String gameName) {
        this.gameID = gameID;
        this.message = message;
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
