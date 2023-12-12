package chess;

// Converted to record just because we aren't using it for now
public record ChessPositionImpl(int row, int column) implements ChessPosition {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return row == that.row && column == that.column;
    }

}
