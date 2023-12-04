package webSeverMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer {
    private Integer gameID;
    private ChessGame.TeamColor teamColor;

    public JoinPlayer(Integer gameID, ChessGame.TeamColor teamColor) {
        this.gameID = gameID;
        this.teamColor = teamColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
