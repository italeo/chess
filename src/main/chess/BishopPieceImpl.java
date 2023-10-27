package chess;

import java.util.Collection;

public class BishopPieceImpl extends ChessPieceImpl {

    public BishopPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.BISHOP);
    }

    @Override
    public ChessPiece copy() {
        return new BishopPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // Define the possible diagonal moves
        int[] rowDirection = {-1, -1, 1, 1};
        int[] colDirection = {-1, 1, -1, 1};

        return generateDirectionalMoves(board, myPosition, rowDirection, colDirection);
    }

}

