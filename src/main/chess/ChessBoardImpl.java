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

    public void removePiece(ChessPosition position) {
        board[position.getRow() - 1][position.getColumn() - 1] = null;
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


        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

    }

    @Override
    public ChessBoard copyBoard() {
        ChessBoard copiedBoard = new ChessBoardImpl();

        // Copy each piece n the board;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    copiedBoard.addPiece(new ChessPositionImpl(row + 1, col + 1), piece.copy());
                }
            }
        }

        return copiedBoard;
    }
}
