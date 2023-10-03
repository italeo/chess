package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {

    private final ChessPositionImpl startPosition;
    private final ChessPositionImpl endPosition;
    private final ChessPiece.PieceType getPiece;


    // Had to type cast because it was showing up as an error in the TestFactory


    public ChessMoveImpl(ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType getPiece) {
        this.startPosition = (ChessPositionImpl) startPos;
        this.endPosition = (ChessPositionImpl) endPos;
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
    public ChessPiece.PieceType getPromotionPiece() { return getPiece; }

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
