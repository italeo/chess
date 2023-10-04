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
        if (ReachesPromotionRow(myPosition)) {

            // Checking for white piece
            if (getTeamColor() == ChessGame.TeamColor.WHITE) {
                validMoves.addAll(getPromotionMoves(myPosition, List.of(PieceType.QUEEN, PieceType.BISHOP, PieceType.ROOK, PieceType.KNIGHT)));
            }
            else if(getTeamColor() == ChessGame.TeamColor.BLACK){
                validMoves.addAll(getPromotionMoves(myPosition, List.of(PieceType.QUEEN, PieceType.BISHOP, PieceType.ROOK, PieceType.KNIGHT)));
            }
        }

        return validMoves;
    }


    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }


    private boolean ReachesPromotionRow(ChessPosition position) {
        return (getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 8) ||
                (getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 1);

    }

    private List<ChessMove> getPromotionMoves(ChessPosition myPosition, List<ChessPiece.PieceType> promotionPieceTypes) {
        List<ChessMove> promotionMoves = new ArrayList<>();
        ChessPosition newPosition = new ChessPositionImpl(myPosition.getRow(), myPosition.getColumn());


        for (ChessPiece.PieceType promotionPiece : promotionPieceTypes) {
            ChessMove promotionMove = new ChessMoveImpl(newPosition, newPosition, promotionPiece);
            promotionMoves.add(promotionMove);

        }

        return promotionMoves;
    }
}
