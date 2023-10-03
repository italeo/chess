package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingPieceImpl extends ChessPieceImpl {

    public KingPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.KING);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Define the possible directions in which the king can move
        // Horizontal, vertical, and diagonal
        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        // Checking if the new move is within the bounds of the board.

        for (int i = 0; i < 8; i++) {
            int newRow = myPosition.getRow() + rowDirections[i];
            int newCol = myPosition.getColumn() + colDirections[i];

            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                // If target piece square is empty or has enemy then it is valid
                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl((ChessPositionImpl) myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }

        }

        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
