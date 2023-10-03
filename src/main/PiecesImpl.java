import chess.*;

import java.util.Collection;

public abstract class PiecesImpl implements ChessPiece {
    private final ChessGame.TeamColor teamColor;
    priavete final PieceType pieceType;

    public PiecesImpl(ChessGame.TeamColor teamColor, PieceType pieceType) {
        this.teamColor = teamColor;
        this.pieceType = pieceType;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
