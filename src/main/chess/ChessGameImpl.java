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
        for (int i = 0; i < allMoves.size(); i++) {
            if (!isInCheck(teamTurn)) {
                finalMoves.add(allMoves.get(i));
            }
        }
        return finalMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        List<ChessMove> moves = (List<ChessMove>) validMoves(move.getStartPosition());
        if (moves.contains(move)) {
            ChessPiece piece = getBoard().getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), piece);
            // FUNCTION NEEDED FOR removePiece()

        }
        else {
            throw  new InvalidMoveException();
        }


    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
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
