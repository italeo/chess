package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessPiece.PieceType.ROOK;

public class PawnPieceImpl extends ChessPieceImpl {

    private final boolean hasMoved;

    public PawnPieceImpl(ChessGame.TeamColor teamColor) {
        super(teamColor, PieceType.PAWN);
        hasMoved = false;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();

        // Pawn moves forward but kills diagonal
        // Define the direction based on the team color
        int direction = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // Defining the possible moves for attacking and moving forward
        int[] rowOffsets = {direction, 2 * direction, direction, direction};
        int[] colOffsets = {0, 0, -1, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = myPosition.getRow() + rowOffsets[i];
            int newCol = myPosition.getColumn() + colOffsets[i];

            if (isValidPosition(newRow, newCol)) {
                ChessPositionImpl newPosition = new ChessPositionImpl(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPosition);

                // Regular move after the initial (one square)
                if (i == 0 && targetPiece == null) {
                    ChessMove move = new ChessMoveImpl(myPosition, newPosition, null);
                    validMoves.add(move);


                    // Pawn should be able to move forward 2 squares on initial move.
                    if (!hasMoved) {
                        int doubleMoveRow = myPosition.getRow() + (2 * direction);
                        if (isValidPosition(doubleMoveRow, newCol) && board.getPiece(new ChessPositionImpl(doubleMoveRow, newCol)) == null) {
                            ChessPosition doubleMovePosition = new ChessPositionImpl(doubleMoveRow, newCol);
                            ChessMove doubleMove = new ChessMoveImpl(myPosition, doubleMovePosition, null);
                            validMoves.add(doubleMove);
                        }
                    }
                }


                // Diagonal capture moves
               if ((i == 2 || i == 3) && targetPiece != null && targetPiece.getTeamColor() != getTeamColor()) {
                   ChessMove captureMove = new ChessMoveImpl(myPosition, newPosition, null);
                   validMoves.add(captureMove);
               }

                // The promotion that happens when the pawn reaches the end of the board
                if (newRow == 1 || newRow == 8) {
                    PromotePawn(validMoves, myPosition, newPosition);
                }
            }
        }

        return validMoves;
    }

    private void PromotePawn(List<ChessMove> validMoves, ChessPosition myPosition, ChessPositionImpl newPosition) {
        ChessGame.TeamColor teamColor = getTeamColor();

        // The promotion options for when the pawn reaches the end of the board (at either ends)
        ChessPiece.PieceType[] promotionPieceTypes = {
                ROOK,
                PieceType.BISHOP,
                PieceType.KNIGHT,
                PieceType.QUEEN,
        };

        for (ChessPiece.PieceType promotionPieceType : promotionPieceTypes) {
            ChessPiece promotionPiece = CreatePromotion(teamColor, promotionPieceType);
            ChessMove promoteMove = new ChessMoveImpl(myPosition, newPosition, promotionPiece.getPieceType());
            validMoves.add(promoteMove);
        }
    }

    private ChessPiece CreatePromotion(ChessGame.TeamColor teamColor, PieceType pieceType) {
        return switch (pieceType) {
            case ROOK -> new RookPieceImpl(teamColor);
            case KNIGHT -> new KnightPieceImpl(teamColor);
            case QUEEN -> new QueenPieceImpl(teamColor);
            case BISHOP -> new BishopPieceImpl(teamColor);
            default -> throw new IllegalArgumentException("Invalid promotion piece type");
        };
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
