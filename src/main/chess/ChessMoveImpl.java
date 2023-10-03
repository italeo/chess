package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {

    private ChessPositionImpl startPosition;
    private ChessPositionImpl endPosition;
    private ChessPieceImpl getPiece;


    public ChessMoveImpl(ChessPositionImpl startPos, ChessPositionImpl endPos, ChessPieceImpl getPiece) {
        this.startPosition = startPos;
        this.endPosition = endPos;
        this.getPiece = getPiece;
    }



    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPieceImpl getPromotionPiece() { return getPiece; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && Objects.equals(getPiece, chessMove.getPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, getPiece);
    }
}
