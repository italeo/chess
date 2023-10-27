package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnPieceImpl extends ChessPieceImpl {

    public PawnPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.PAWN);
    }

    @Override
    public ChessPiece copy() {
        return new PawnPieceImpl(getTeamColor());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Pawn moves forward but kills diagonal
        // Define the direction based on the team color
        int direction = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int newRow = myPosition.getRow() + direction;
        int newCol = myPosition.getColumn();

        if (isValidPosition(newRow, newCol) && board.getPiece(new ChessPositionImpl(newRow, newCol)) == null) {
            ChessPosition newPosition = new ChessPositionImpl(newRow, newCol);
            ChessMove move = new ChessMoveImpl(myPosition, newPosition, null);
            validMoves.add(move);

            // Check for double move on the pawn's first move
            if ((getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                    (getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
                int doubleMoveRow = myPosition.getRow() + (2 * direction);
                int doubleMoveCol = myPosition.getColumn();

                if (isValidPosition(doubleMoveRow, doubleMoveCol) && board.getPiece(new ChessPositionImpl(doubleMoveRow,
                        doubleMoveCol)) == null) {
                    ChessPosition doubleMovePosition = new ChessPositionImpl(doubleMoveRow, doubleMoveCol);
                    ChessMove doubleMove = new ChessMoveImpl(myPosition, doubleMovePosition, null);
                    validMoves.add(doubleMove);
                }

            }
        }

        // Checking for diagonal capture
        // Left and right
        int[] colOffsets = {-1, 1};
        for (int colOffset : colOffsets) {
            newRow = myPosition.getRow() + direction;
            newCol = myPosition.getColumn() + colOffset;

            if (isValidPosition(newRow, newCol)) {
                ChessPosition newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);


                if (targetPiece != null && targetPiece.getTeamColor() != getTeamColor()) {
                    ChessMove captureMove = new ChessMoveImpl(myPosition, newPosition, null);
                    validMoves.add(captureMove);
                }
            }
        }

        // Handling the promotion when a Pawn gets to the opponents end of the board
        List<ChessMove> promotions = new ArrayList<>();
        if (ReachesPromotionRow(myPosition)) {
            for (ChessMove move : validMoves) {
                getPromotionPieces(move, promotions);
            }
            return promotions;
        }
        return validMoves;

    }

    private boolean ReachesPromotionRow(ChessPosition myPosition) {
        return (getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) ||
                (getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2);
    }

    private void getPromotionPieces(ChessMove move, List<ChessMove> promotions) {
        promotions.add(new ChessMoveImpl(move.getStartPosition(), move.getEndPosition(), PieceType.QUEEN));
        promotions.add(new ChessMoveImpl(move.getStartPosition(), move.getEndPosition(), PieceType.BISHOP));
        promotions.add(new ChessMoveImpl(move.getStartPosition(), move.getEndPosition(), PieceType.ROOK));
        promotions.add(new ChessMoveImpl(move.getStartPosition(), move.getEndPosition(), PieceType.KNIGHT));

    }
}
