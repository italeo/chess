package chess;


import java.util.Collection;

public class KnightPieceImpl extends ChessPieceImpl {

    public KnightPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
