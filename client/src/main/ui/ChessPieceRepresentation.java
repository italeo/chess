package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class ChessPieceRepresentation {
    /** Return the string representation of the chess piece given the piece type and color.*/

    public static String getPieceSymbol(ChessPiece.PieceType pieceType, ChessGame.TeamColor teamColor) {
        String colorSuffix = (teamColor == ChessGame.TeamColor.BLACK) ? "B" : "W";
        switch (pieceType) {
            case KING:
                return "K" + colorSuffix;
            case QUEEN:
                return "Q" + colorSuffix;
            case BISHOP:
                return "B" + colorSuffix;
            case KNIGHT:
                return "N" + colorSuffix;
            case ROOK:
                return "R" + colorSuffix;
            case PAWN:
                return "P" + colorSuffix;
            default:
                return "?";
        }
    }
}
