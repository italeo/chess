package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingPieceImpl extends ChessPieceImpl {

    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;
    public KingPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.KING);
    }

    @Override
    public ChessPiece copy() {
        return new KingPieceImpl(getTeamColor());
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
            int newRow = myPosition.row() + rowDirections[i];
            int newCol = myPosition.column() + colDirections[i];

            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                // If target piece square is empty or has enemy then it is valid
                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl(myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }

        }
        return validMoves;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= BOARD_MIN && row <= BOARD_MAX && col >= BOARD_MIN && col <= BOARD_MAX;
    }
}
