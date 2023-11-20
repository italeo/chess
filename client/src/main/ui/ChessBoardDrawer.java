package ui;

import chess.*;

public class ChessBoardDrawer {
    private ChessGame game = new ChessGameImpl();
    private ChessBoard board;

    public void drawBoard(Integer gameID) {
        if ( gameID != null) {

            // Get the board
            this.board = game.getBoard();

            System.out.print(EscapeSequences.ERASE_SCREEN);

            // Iterate through the board and draw the board.
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition position = new ChessPositionImpl(row, col);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null) {
                        String pieceSymbol = ChessPieceRepresentation.getPieceSymbol(piece.getPieceType(), piece.getTeamColor());
                        String textColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_TEXT_COLOR_BLACK : EscapeSequences.SET_TEXT_COLOR_WHITE;
                        String bgColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                        System.out.print(textColor + bgColor + pieceSymbol + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR + " ");
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY + EscapeSequences.EMPTY + EscapeSequences.RESET_TEXT_COLOR + " ");
                    }
                }
                System.out.println();
            }
        }
    }
}
