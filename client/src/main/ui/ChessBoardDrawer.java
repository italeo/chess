package ui;
import chess.*;

public class ChessBoardDrawer {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String WHITE_SQUARE = EscapeSequences.SET_BG_COLOR_WHITE;
    private static final String BLACK_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK;
    private static final String BORDER_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;



    private final ChessGame game = new ChessGameImpl();

    public void drawBoard(Integer gameID) {

        if ( gameID != null) {

            // Get the board
            ChessBoard board = game.getBoard();

            System.out.print(EscapeSequences.ERASE_SCREEN);

            // Print top headers with column letters
            drawColLetterHeader();

            // Iterate through the board and draw the board.
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
                // Draw the rows with number header
                drawRowNumberHeader(row);

                for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; col++) {
                    drawChessSquare(row, col);
                    System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);

                }

                drawRowNumberHeader(row);
                System.out.println();
            }
            // Draw the other column header
            drawColLetterHeader();
        }
    }

    private void drawChessSquare(int row, int col) {
        if ((row + col) % 2 == 0) {
            System.out.print(WHITE_SQUARE);
        } else {
            System.out.print(BLACK_SQUARE);
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
            System.out.print(" " + (char) ('h' - col + 1) + " ");
            System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
        }
        drawExtraSquare(BORDER_COLOR); // Top right corner
        System.out.println();
    }

    private void drawRowNumberHeader(int row) {
        System.out.print(BORDER_COLOR);
        System.out.print(" " + row + " ");
        System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
    }
}
