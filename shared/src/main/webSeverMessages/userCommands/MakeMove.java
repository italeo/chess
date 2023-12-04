package webSeverMessages.userCommands;

import chess.ChessMove;

public class MakeMove {
    private Integer gameID;
    private ChessMove move;

    public MakeMove(Integer gameID, ChessMove move) {
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
