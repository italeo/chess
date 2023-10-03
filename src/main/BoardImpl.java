import chess.*;
import java.util.HashMap;
import java.util.Map;


public class BoardImpl implements ChessBoard {

    private final Map<ChessPosition, ChessPiece> board;

    public BoardImpl() {
        this.board = new HashMap<>();
        resetBoard();
    }


    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);

    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    @Override
    public void resetBoard() {
        resetBoard();

        addPiece(new ChessPosition('a', 1), new Rook(ChessPieceColor.WHITE));



    }
}
