package webSeverMessages.userCommands;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMove extends UserGameCommand {
    private Integer gameID;
    private ChessMove move;

    public MakeMove(Integer gameID, ChessMove move) {
        super(String.valueOf(CommandType.MAKE_MOVE));
        this.gameID = gameID;
        this.move = move;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
