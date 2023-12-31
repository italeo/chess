package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotePiece;

    // Initializes the chess moves with specific start and end positions
    public ChessMoveImpl(ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType promotePiece) {
        this.startPosition = startPos;
        this.endPosition = endPos;
        this.promotePiece = promotePiece;
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
    public ChessPiece.PieceType getPromotionPiece() {
        return promotePiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition,
                chessMove.endPosition) && promotePiece == chessMove.promotePiece;
    }

    // Calculates the hash code based on the start, end, and promoted piece positions
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotePiece);
    }
}
