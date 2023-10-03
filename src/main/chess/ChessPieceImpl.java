package chess;

import java.util.Collection;

public abstract class ChessPieceImpl implements ChessPiece {

    private final ChessGame.TeamColor teamColor;

    private final PieceType type;

    public ChessPieceImpl(ChessGame.TeamColor teamColor, PieceType type) {
        this.teamColor = teamColor;
        this.type = type;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
