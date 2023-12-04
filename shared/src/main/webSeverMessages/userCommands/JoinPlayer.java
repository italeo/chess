package webSeverMessages.userCommands;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayer extends UserGameCommand {
    private Integer gameID;
    private ChessGame.TeamColor teamColor;

    public JoinPlayer(Integer gameID, ChessGame.TeamColor teamColor) {
        super(String.valueOf(CommandType.JOIN_PLAYER));
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
