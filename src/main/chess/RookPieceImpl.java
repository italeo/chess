package chess;

import java.util.Collection;

public class RookPieceImpl extends ChessPieceImpl {

    public RookPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.ROOK);
    }

    @Override
    public ChessPiece copy() {
        return new RookPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // Defines the possible direction that the Rook can move.
        // Which is Horizontal and Vertical
        int[] rowDirections = {-1, 1, 0, 0};
        int[] colDirections = {0, 0, -1, 1};

        return generateDirectionalMoves(board, myPosition, rowDirections, colDirections);
    }
}
