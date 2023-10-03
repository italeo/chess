package chess;

import chess.*;

import java.util.Collection;

public class PawnPieceImpl extends ChessPieceImpl {

    public PawnPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.PAWN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
