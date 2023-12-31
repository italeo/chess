package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightPieceImpl extends ChessPieceImpl {
    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;

    public KnightPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.KNIGHT);
    }

    @Override
    public ChessPiece copy() {
        return new KnightPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Defining all possible moves for the knight
        // offsets in shape of an 'L'
        int[] rowOffsets = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] colOffsets = {-1, 1, -2, 2, -2, 2, 1, -1};

        for (int i = 0; i < 8; i++) {
            int newRow = myPosition.row() + rowOffsets[i];
            int newCol = myPosition.column() + colOffsets[i];

            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl( myPosition, newPosition, null);
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
