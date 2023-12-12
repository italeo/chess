package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenPieceImpl extends ChessPieceImpl {

    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;

    public QueenPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.QUEEN);
    }

    @Override
    public ChessPiece copy() {
        return new QueenPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        List<ChessMove> validMoves = new ArrayList<>();
        // Defining the directions that the queen is able to move
        // Horizontal, vertical, diagonal
        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
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
