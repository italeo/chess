package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookPieceImpl extends ChessPieceImpl {

    public RookPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.ROOK);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Defines the possible direction that the Rook can move.
        // Which is Horizontal and Vertical

        int[] rowDirections = {-1, 1, 0, 0};
        int[] colDirections = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = myPosition.getRow() + rowDirections[i];
            int newCol = myPosition.getColumn() + colDirections[i];

            // Checking to see if the new position is within the bounds of the board.
            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);


                // Checking if the targeted square if empty or has an enemy piece. If it is, then it is valid.
                // ** Possible error should I be typecasting????
                if(targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl((ChessPositionImpl) myPosition, newPosition, null);
                    validMoves.add(move);
                }

                // If there is a piece in the way, then break out of query in that direction
                if (targetPiece!= null) {
                    break;
                }
            }
        }

        return validMoves;
    }

    public boolean isValidPosition(int row, int col) {

        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }


}
