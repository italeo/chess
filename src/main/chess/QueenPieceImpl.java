package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenPieceImpl extends ChessPieceImpl {

    public QueenPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.QUEEN);
    }

    @Override
    public ChessPiece copy() {
        return new QueenPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // Defining the directions that the queen is able to move
        // Horizontal, vertical, diagonal
        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};


        return generateDirectionalMoves(board, myPosition, rowDirections, colDirections);
    }

}
