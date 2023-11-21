package ui;
import chess.*;

public class ChessBoardDrawer {

    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String WHITE_SQUARE = EscapeSequences.SET_BG_COLOR_WHITE + " ";
    private static final String BLACK_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK + " ";
    private static final String BORDER_COLOR = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;



    private final ChessGame game = new ChessGameImpl();

    public void drawBoard(Integer gameID) {

        if ( gameID != null) {

            // Get the board
            ChessBoard board = game.getBoard();

            System.out.print(EscapeSequences.ERASE_SCREEN);

            // Iterate through the board and draw the board.
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
                for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; col++) {
                    drawChessSquare(row, col);
                    System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);

                }
                System.out.print(EscapeSequences.RESET_TO_TERMINAL_DEFAULT);
                System.out.println();
            }
        }
    }

    private void drawChessSquare(int row, int col) {
        if ((row + col) % 2 == 0) {
            setWhite();
            System.out.print(WHITE_SQUARE);
        } else {
            setBlack();
            System.out.print(BLACK_SQUARE);
        }

        if (col < BOARD_SIZE_IN_SQUARES) {
            setBlack();
            System.out.print(EscapeSequences.EMPTY);
        }
    }

    private void setBlack() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private void setWhite() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }
}
