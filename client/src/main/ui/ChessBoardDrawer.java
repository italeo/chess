package ui;

import chess.*;
import model.Game;
import server.SessionManager;

public class ChessBoardDrawer {
    private ChessGame game = new ChessGameImpl();
    private ChessBoard board;

    public void drawBoard(Game game) {

        Integer gameID = SessionManager.getGameID();
        if ( gameID != null) {

            // Get the board
            // NOT THE RIGHT WAY
            this.board = game.getGame().getBoard();

            // Iterate through the board and draw the board.
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition position = new ChessPositionImpl(row, col);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null) {
                        System.out.println(ChessPieceRepresentation.getPieceSymbol(piece.getPieceType(), piece.getTeamColor()) + " ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
        }
    }
}
