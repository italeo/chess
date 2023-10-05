package chess;


public class ChessBoardImpl implements ChessBoard {

    private final ChessPiece[][] board;

    public ChessBoardImpl() {
        board = new ChessPieceImpl[8][8];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        for (int i = 0; i < 8; i++) {
            board[6][i] = new PawnPieceImpl(ChessGame.TeamColor.BLACK);
            board[1][i] = new PawnPieceImpl(ChessGame.TeamColor.WHITE);
        }

        board[7][0] = new RookPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][0] = new RookPieceImpl(ChessGame.TeamColor.WHITE);
        board[7][7] = new RookPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][7] = new RookPieceImpl(ChessGame.TeamColor.WHITE);

        board[7][1] = new KnightPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][1] = new KnightPieceImpl(ChessGame.TeamColor.WHITE);
        board[7][6] = new KnightPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][6] = new KnightPieceImpl(ChessGame.TeamColor.WHITE);

        board[7][2] = new BishopPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][2] = new BishopPieceImpl(ChessGame.TeamColor.WHITE);
        board[7][5] = new BishopPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][5] = new BishopPieceImpl(ChessGame.TeamColor.WHITE);


        board[7][3] = new QueenPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][3] = new QueenPieceImpl(ChessGame.TeamColor.WHITE);

        board[7][4] = new KingPieceImpl(ChessGame.TeamColor.BLACK);
        board[0][4] = new KingPieceImpl(ChessGame.TeamColor.WHITE);

    }
}
