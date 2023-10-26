package request;

import chess.ChessGame;

/** Represents the request of joining a game. */
public class JoinGameRequest {
    private String playerColor;
    private int gameID;
    private String authToken;


    public JoinGameRequest(String authToken, String playerColor, int gameID) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }
}
