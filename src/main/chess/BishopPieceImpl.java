package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopPieceImpl extends ChessPieceImpl {

    public BishopPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.BISHOP);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Define the possible diagonal moves
        int[] rowDirection = {-1, -1, 1, 1};
        int[] colDirection = {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = myPosition.getRow() + rowDirection[i];
            int newCol = myPosition.getColumn() + colDirection[i];

            while(isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove move = new ChessMoveImpl((ChessPositionImpl) myPosition, newPosition, null);
                    validMoves.add(move);

                }

                if (targetPiece != null) {
                    break;
                }

                newRow += rowDirection[i];
                newCol += colDirection[i];
            }
        }
        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <=8;
    }
}

