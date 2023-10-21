package request;

import chess.ChessGame;

/** Represents the request of joining a game. */
public class JoinGameRequest {
    private ChessGame.TeamColor playerColor;
    private int gameID;

    public JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
