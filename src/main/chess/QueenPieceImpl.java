package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenPieceImpl extends ChessPieceImpl {

    public QueenPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.QUEEN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Defining the directions that the queen is able to move
        // Horizontal, vertical, diagonal
        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = myPosition.getRow() + rowDirections[i];
            int newCol = myPosition.getColumn() + colDirections[i];

            while(isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl( myPosition, newPosition, null);
                    validMoves.add(move);
                }


                // If there is another one of your piece in the way then stop searching
                if (targetPiece != null) {
                    break;
                }

                newRow += rowDirections[i];
                newCol += colDirections[i];
            }
        }

        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <=8;
    }
}
