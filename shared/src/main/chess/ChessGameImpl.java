package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChessGameImpl implements ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board = new ChessBoardImpl();
    private boolean makeEndOfGame = false;

    public ChessGameImpl() {
        this.teamTurn = TeamColor.WHITE;
        board.resetBoard();
    }


//    public boolean isMarkEndOfGame() {
//        return makeEndOfGame;
//    }
//
//    public void setMakeEndOfGame(boolean makeEndOfGame) {
//        this.makeEndOfGame = makeEndOfGame;
//    }

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
        ChessPiece piece = board.getPiece(startPosition);

        // Checking if there is no piece in the starting position
        if (piece == null) {
            return Collections.emptyList();
        }

        // Generates the list of all possible moves for the piece if there is one in the starting position
        List<ChessMove> moves = (List<ChessMove>) piece.pieceMoves(board, startPosition);
        for (ChessMove move : moves) {
           // ChessBoard temp = tempBoard(allMove, board);
            board.addPiece(startPosition, null);
            ChessPiece piecePosition = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            if (!isInCheck(piece.getTeamColor())) {
                finalMoves.add(move);
            }
            board.addPiece(startPosition, piece);
            board.addPiece(move.getEndPosition(), piecePosition);
        }
        return finalMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        List<ChessMove> moves = (List<ChessMove>) validMoves(move.getStartPosition());
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());

        if (moves.contains(move) && piece != null && piece.getTeamColor() == teamTurn) {

            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(move.getStartPosition());

            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

            // Checking for pawn promotion
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                int promotionRank = (piece.getTeamColor() == TeamColor.WHITE) ? 8 : 1;
                if (move.getEndPosition().row() == promotionRank) {
                    // Performs the promotion
                    ChessPiece promotedPiece = createPromotedPiece(move.getPromotionPiece(), piece.getTeamColor());
                    board.addPiece(move.getEndPosition(), promotedPiece);
                }
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    private ChessPiece createPromotedPiece(ChessPiece.PieceType type, TeamColor teamColor) {
        return switch (type) {
            case ROOK -> new RookPieceImpl(teamColor);
            case BISHOP -> new BishopPieceImpl(teamColor);
            case KNIGHT -> new KnightPieceImpl(teamColor);
            default -> new QueenPieceImpl(teamColor);
        };
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition realKingPos = null;
        List<ChessMove> piecesPosition = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPositionImpl(row, col);
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

                if (TeamCheck(teamColor, startPosition, piece)) return false;
            }
        }
        return true;
    }

    private boolean TeamCheck(TeamColor teamColor, ChessPosition startPosition, ChessPiece piece) {
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
                if(!isInCheck(teamColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            // If the King is in check then it cannot be in stalemate
            return false;
        }

        //Iterate through the piece of the team.
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition startPosition = new ChessPositionImpl(row, col);
                ChessPiece piece = board.getPiece(startPosition);

                if (TeamCheck(teamColor, startPosition, piece)) return false;
            }
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean isMarkEndFfGame() {
        return makeEndOfGame;
    }

    @Override
    public void setMarkEndOfGame(boolean markEndOfGame) {
        this.makeEndOfGame = markEndOfGame;
    }
}
