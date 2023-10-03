package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnPieceImpl extends ChessPieceImpl {

    public PawnPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.PAWN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Pawn moves forward but kills diagonal
        // Define the direction based on the team color
        int direction = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // Defining the possible moves for attacking and moving forward
        int[] rowOffsets = {direction, 2 * direction};
        int[] colOffsets = {0, 0};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = myPosition.getRow() + rowOffsets[i];
            int newCol = myPosition.getColumn() + colOffsets[i];

            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null) {
                    ChessMove move = new ChessMoveImpl((ChessPositionImpl) myPosition, newPosition, null);
                    validMoves.add(move);
                }

                // Pawn should be able to move forward 2 squares on initial move.
                if (i == 0 && targetPiece == null) {
                    int doubleMoveRow = myPosition.getRow() + (2 * direction);
                    if (isValidPosition(doubleMoveRow, newCol) && board.getPiece(new ChessPositionImpl(doubleMoveRow,newCol)) == null) {
                        ChessPositionImpl doubleMovePosition = new ChessPositionImpl(doubleMoveRow, newCol);
                        ChessMove doubleMove = new ChessMoveImpl((ChessPositionImpl) myPosition, doubleMovePosition, null);
                        validMoves.add(doubleMove);
                    }
                }
            }
        }

        int[] capturedRowOffsets = {direction, direction};
        int[] capturedColOffsets = {-1, 1};

        for (int i = 0; i < capturedColOffsets.length; i++) {
            int newRow = myPosition.getRow() + capturedRowOffsets[i];
            int newCol = myPosition.getColumn() + capturedColOffsets[i];

            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece != null && targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove captureMove = new ChessMoveImpl((ChessPositionImpl) myPosition, newPosition, null);
                    validMoves.add(captureMove);
                }
            }
        }

        return validMoves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
