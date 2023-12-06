package webSeverMessages.userCommands;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayer extends UserGameCommand {
    private Integer gameID;
    private ChessGame.TeamColor playerColor;

    public JoinPlayer(Integer gameID, ChessGame.TeamColor playerColor) {
        super(String.valueOf(CommandType.JOIN_PLAYER));
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getTeamColor() {
        return playerColor;
    }
}
