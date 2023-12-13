package ui;

import chess.*;
import model.Game;

public class ChessBoardDrawer {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String WHITE_SQUARE = EscapeSequences.SET_BG_COLOR_WHITE;
    private static final String BLACK_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK;
    private static final String BORDER_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;


    private final ChessGame game = new ChessGameImpl();


    public void drawBoard(Game game) {
        if (game != null && game.getGame().getBoard() != null) {
            System.out.print(EscapeSequences.ERASE_SCREEN);

            // Get the chessBoard
            ChessBoard chessBoard = game.getGame().getBoard();
            ChessGame.TeamColor playerColor = game.getGame().getTeamTurn();
            Integer gameID = game.getGameID();

            // Determine which board to draw depending on the color
            if ((playerColor == ChessGame.TeamColor.WHITE) || (playerColor == ChessGame.TeamColor.BLACK)) {
                if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
                    drawBoardWhite(gameID);
                } else {
                    drawBoardWhite(gameID);
                }
            } else {
                drawBoardWhite(gameID);
            }
        }
    }

    public void drawBoardWhite(Integer gameID) {

        if (gameID != null) {

            // Get the board
            ChessBoard board = game.getBoard();

            System.out.print(EscapeSequences.ERASE_SCREEN);

            // Print top headers with column letters
            drawColLetterHeader();

            // Iterate through the board and draw the board.
            for (int row = BOARD_SIZE_IN_SQUARES; row >= 1; row--) {
                // Draw the rows with number header
                drawRowNumberHeader(row);

                for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; col++) {
                    drawChessSquare(row, col, board);
                    System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);

                }

                drawRowNumberHeader(row);
                System.out.println();
            }
            // Draw the other column header
            drawColLetterHeader();
        }
    }

    private void drawChessSquare(int row, int col, ChessBoard board) {
        if ((row + col) % 2 == 0) {
            System.out.print(WHITE_SQUARE);
        } else {
            System.out.print(BLACK_SQUARE);
        }

        ChessPosition position = new ChessPositionImpl(row, col);
        ChessPiece piece = board.getPiece(position);

        if (piece != null) {
            String pieceSymbol = getPieceSymbols(piece);
            String pieceColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.SET_TEXT_COLOR_MAGENTA
                    : EscapeSequences.SET_TEXT_COLOR_RED;
            System.out.print(pieceColor + pieceSymbol + EscapeSequences.SET_TEXT_BOLD);
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
        System.out.print(EscapeSequences.EMPTY);
    }

    private void drawExtraSquare(String color) {
        System.out.print(color);
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }

    private void drawColLetterHeader() {
        System.out.print(BORDER_COLOR);
        drawExtraSquare(BORDER_COLOR); //Top left corner
        for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; col++) {
            System.out.print(BORDER_COLOR);
            System.out.print(" " + " " + " " + (char) ('h' - col + 1) + " " + " ");
            System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
        }
        drawExtraSquare(BORDER_COLOR); // Top right corner
        System.out.println();
    }

    private void drawRowNumberHeader(int row) {
        System.out.print(BORDER_COLOR);
        System.out.print(" " + (BOARD_SIZE_IN_SQUARES - row + 1) + " ");
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }


    // ----------------------- FLIPPED BOARD ----------------------------------------
    public void drawBoardBlack(Integer gameID) {
        if (gameID != null) {
            ChessBoard board = game.getBoard();

            System.out.print(EscapeSequences.ERASE_SCREEN);

            // Print top headers with column letters
            drawColLetterHeaderFlipped();

            // Iterate through the board and draw the board.
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
                // Draw the rows with number header
                drawRowNumberHeaderFlipped(row);

                for (int col = BOARD_SIZE_IN_SQUARES; col >= 1; col--) {
                    drawChessSquare(row, col, board);
                    System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);

                }

                drawRowNumberHeaderFlipped(row);
                System.out.println();
            }
            // Draw the other column header
            drawColLetterHeaderFlipped();

        }
    }

    public void drawColLetterHeaderFlipped() {
        System.out.print(BORDER_COLOR);
        drawExtraSquare(BORDER_COLOR); //Top left corner
        for (int col = BOARD_SIZE_IN_SQUARES; col >= 1; col--) {
            System.out.print(BORDER_COLOR);
            System.out.print(" " + " " + " " + (char) ('h' - col + 1) + " " + " ");
            System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
        }
        drawExtraSquare(BORDER_COLOR); // Top right corner
        System.out.println();
    }

    public void drawRowNumberHeaderFlipped(int row) {
        System.out.print(BORDER_COLOR);
        System.out.print(" " + (BOARD_SIZE_IN_SQUARES - row + 1) + " ");
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }

    private String getPieceSymbols(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
    }
}
