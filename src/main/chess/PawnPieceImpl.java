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

                if (isValidPosition(doubleMoveRow, doubleMoveCol) && board.getPiece(new ChessPositionImpl(doubleMoveRow, doubleMoveCol)) == null) {
                    ChessPosition doubleMovePosition = new ChessPositionImpl(doubleMoveRow, doubleMoveCol);
                    ChessMove doubleMove = new ChessMoveImpl(myPosition, doubleMovePosition, null);
                    validMoves.add(doubleMove);
                }

            }
        }
        return validMoves;
    }


    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
