package result;

import chess.ChessGame;

/** Represents the result of creating a new chess game. */
public class CreateGameResult {
    private final boolean success;
    private final String message;
    private final ChessGame createdGame;

    /** Constructs new game from the request.
     * @param success - The boolean will return true if the game was created correctly according to the rules that were defined.
     * @param message - Returns a message informing the user if the game was created or not.
     * @param createdGame - The new game object create.
     * */
    public CreateGameResult(boolean success, String message, ChessGame createdGame, int gameID) {
        this.success = success;
        this.message = message;
        this.createdGame = createdGame;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ChessGame getCreatedGame() {
        return createdGame;
    }

}
