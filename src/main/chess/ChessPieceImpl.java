package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ChessPieceImpl implements ChessPiece {
    private final ChessGame.TeamColor teamColor;
    private final PieceType type;
    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;

    public ChessPieceImpl(ChessGame.TeamColor teamColor, PieceType type) {
        this.teamColor = teamColor;
        this.type = type;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }


    // Only use this function for Rook, Queen, and Bishop
    //King and Knight do not need to update/increment the row and column directions
    protected Collection<ChessMove> generateDirectionalMoves(ChessBoard board, ChessPosition myPosition, int[] rowDirections, int[] colDirections) {
        List<ChessMove> validMoves = new ArrayList<>();

        for (int i = 0; i < rowDirections.length; i++) {
            int newRow = myPosition.getRow() + rowDirections[i];
            int newCol = myPosition.getColumn() + colDirections[i];

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

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
