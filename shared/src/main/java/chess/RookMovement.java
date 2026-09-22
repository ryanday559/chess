package chess;

public class RookMovement implements PieceMovement{
    @Override
    public int[][] getLoopOffsets() {
        int[][] offsets = {
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };
        return offsets;
    }
}
