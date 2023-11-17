package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessPositionImpl;
import dao.GameDAO;
import dataAccess.DataAccessException;
import model.Game;
import server.SessionManager;

public class ChessBoardDrawer {
    private Game game;
    private GameDAO gameDAO = new GameDAO();
    private ChessBoard board;

    public void drawBoard(Game game) throws DataAccessException {
        try {
            if (SessionManager.getGameID() != null) {
                // Find the correct game
                this.game = gameDAO.findGameByID(SessionManager.getGameID());

                // Get the board
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
        } catch (DataAccessException ex) {
            ex.getMessage();
        }
    }
}
