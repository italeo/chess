package model;
import chess.ChessGame;

/** Sets the structure of the Chess game with the default rules that have been implemented.
 * */
public class Game {
    /** the unique ID for the game.*/
    private int gameID;
    /** The white player's identifier. */
    private String whiteUsername;
    /** The black player's identifier. */
    private String blackUsername;
    /** name of the current chess game. */
    private String gameName;
    /** The implementation of the game. */
    private ChessGame game;
    private boolean markEndOfGame = false;

    public void setMarkEndOfGame(boolean markEndOfGame) {
        this.markEndOfGame = markEndOfGame;
    }

    public boolean isMarkEndOfGame() {
        return markEndOfGame;
    }

    public Game() {
    }

    /** Constructs a game with default setting or basic details.
     * @param gameID - The game identifier
     * @param whiteUsername - The white player identifier.
     * @param blackUsername - The black player identifier.
     * @param gameName - A JSON representation of the chess game state.
     * @param game - The actual game.
     * */


    public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public ChessGame getGame() { return game; }

    public void setGame(ChessGame game) {
        this.game = game;
    }

}
