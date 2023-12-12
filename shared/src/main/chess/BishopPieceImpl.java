package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopPieceImpl extends ChessPieceImpl {
    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;

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
        List<ChessMove> validMoves = new ArrayList<>();

        // Define the possible diagonal moves
        // To generate the directions, call the general function from the ChessPiece class
        int[] rowDirections = {-1, -1, 1, 1};
        int[] colDirections = {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = myPosition.row() + rowDirections[i];
            int newCol = myPosition.column() + colDirections[i];

            while(isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl(myPosition, newPosition, null);
                    validMoves.add(move);
                }

                if (targetPiece != null) {
                    break;
                }
                newRow += rowDirections[i];
                newCol += colDirections[i];
            }
        }
        return validMoves;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= BOARD_MIN && row <= BOARD_MAX && col >= BOARD_MIN && col <= BOARD_MAX;
    }
}

