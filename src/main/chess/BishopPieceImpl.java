package chess;

import chess.*;

import java.util.Collection;

public class BishopPieceImpl extends ChessPieceImpl {

    public BishopPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.BISHOP);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
