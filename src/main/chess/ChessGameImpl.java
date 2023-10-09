package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChessGameImpl implements ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board = new ChessBoardImpl();

    public ChessGameImpl() {
        this.teamTurn = TeamColor.WHITE;
    }

    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;

    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> finalMoves = new ArrayList<>();
        ChessPiece piece = getBoard().getPiece(startPosition);
        List<ChessMove> allMoves = (List<ChessMove>) piece.pieceMoves(board, startPosition);

        for (ChessMove allMove : allMoves) {
            ChessBoard temp = tempBoard(allMove, board);
            if (!isInCheck(teamTurn)) {
                finalMoves.add(allMove);
            }
        }
        return finalMoves;
    }

    private ChessBoard tempBoard(ChessMove move, ChessBoard board) {
        ChessBoard tempBoard = new ChessBoardImpl();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <=8; col++) {
                ChessPosition tempPosition = new ChessPositionImpl(row, col);
                tempBoard.addPiece(tempPosition, board.getPiece(tempPosition));
            }
        }
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        tempBoard.addPiece(move.getEndPosition(), piece);
        tempBoard.removePiece(move.getStartPosition());

        return tempBoard;
    }


    // Might need to work on this portion of the code with having it being able to switch the teams from Black
    // to white or vice-versa
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        List<ChessMove> moves = (List<ChessMove>) validMoves(move.getStartPosition());
        if (moves.contains(move)) {
            ChessPiece piece = getBoard().getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(move.getStartPosition());

        }
        else {
            throw  new InvalidMoveException();
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition realKingPos = null;
        List<ChessMove> piecesPosition = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPositionImpl(i, j);
                ChessPiece possibleKing = board.getPiece(position);

                if (possibleKing == null) {
                    continue;
                }
                else if (possibleKing.getTeamColor() == teamColor && possibleKing.getPieceType() ==
                        ChessPiece.PieceType.KING) { realKingPos = position; }
                else if (possibleKing.getTeamColor() != teamColor) {
                    piecesPosition.addAll(possibleKing.pieceMoves(board, position));
                }
            }
        }

        for (ChessMove chessMove : piecesPosition) {
            if (realKingPos != null && realKingPos.equals(chessMove.getEndPosition())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {

        if (!isInCheck(teamColor)) {
            // If not in check then it's not in checkmate
            return false;
        }

        // Iterate through the white pieces
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition startPosition = new ChessPositionImpl(row, col);
                ChessPiece piece = getBoard().getPiece(startPosition);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    // Generate the valid moves for the piece
                    Collection<ChessMove> validMoves = validMoves(startPosition);

                    // Try each valid moves to see if it gets out of check
                    for (ChessMove move : validMoves) {
                        // Create a copy of the board to simulate the move
                        ChessBoard tempBoard = board.copyBoard();

                        // Apply the move to the copy board
                        tempBoard.addPiece(move.getEndPosition(), piece);
                        tempBoard.removePiece(move.getStartPosition());

                        // Check if the king is still in check
                        if(!isInCheck(teamColor)) { return false; }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;

    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
