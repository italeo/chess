package model;

import dao.DatabaseException;

/** Sets the structure of the Chess game with the default rules that have been implemented.
 * */
public class Game {
    /** the unique ID for the game.*/
    private int gameID;
    /** The white player's identifier. */
    private String whitePlayer;
    /** The black player's identifier. */
    private String blackPlayer;
    /** Could be used as a JSON representation for the chess game state. */
    private String chessGame;

    /** Constructs a game with default setting or basic details.
     * @param gameID - The game identifier
     * @param whitePlayer - The white player identifier.
     * @param blackPlayer - The black player identifier.
     * @param chessGame - A JSON representation of the chess game state.*/
    public Game(int gameID, String whitePlayer, String blackPlayer, String chessGame) {
        this.gameID = gameID;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.chessGame = chessGame;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public String getChessGame() {
        return chessGame;
    }

    public void setChessGame(String chessGame) {
        this.chessGame = chessGame;
    }
}
