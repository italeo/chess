package chess;

import java.util.Collection;

public class BishopPieceImpl extends ChessPieceImpl {

    // Initializes bishop with specific team color
    public BishopPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.BISHOP);
    }

    // Creates a copy of the bishop with the specified team color
    @Override
    public ChessPiece copy() {
        return new BishopPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // Define the possible diagonal moves
        // To generate the directions, call the general function from the ChessPiece class
        int[] rowDirection = {-1, -1, 1, 1};
        int[] colDirection = {-1, 1, -1, 1};

        // Calls the common function to generate the specified directional move for the bishop piece.
        return generateDirectionalMoves(board, myPosition, rowDirection, colDirection);
    }

}

