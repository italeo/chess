package ui;

public class chessBoardDrawer {
    public static void drawChessBoard(boolean whiteAtBottom) {
        char[][] chessboard = createInitialChessBoard();

        if (whiteAtBottom) {
            // Draw the chess board with the WHITE piece at the bottom
            drawChessBoard(chessboard);
        } else {
            // Flip the board and draw with BLACK pieces at the bottom
        }
    }

    private static void drawChessBoard(char[][] chessBoard) {
        // Print the chess board to the console
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static char[][] createInitialChessBoard() {
        char chessBoard[][] = new char[8][8];

        // Initialize the chess board with the start positions
        char[] pieces = {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};
        for (int i = 0; i < 8; i++) {
            chessBoard[0][i] = Character.toUpperCase(pieces[i]);        // Sets BLACK piece in the first row
            chessBoard[1][i] = 'p';                                     // BLACK pawn in the second row
            chessBoard[6][i] = 'P';                                     // Sets WHITE pawn at the bottom of the board
            chessBoard[7][i] = pieces[i];                               // Setting the white pieces

            // Create empty spaces in the middle
            for (int j = 2; j < 6; j++) {
                chessBoard[i][j] = ' ';
            }
        }
        return chessBoard;
    }

    private static char[][] flipChessBoard(char[][] chessBoard) {
        // flip the chess board vertically?
        char[][] flippedBoard = new char[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flippedBoard[i][j] = chessBoard[7 - i][j];
            }
        }

        return flippedBoard;
    }
}
