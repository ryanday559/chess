package chess;

public class BishopMovement implements PieceMovement{
    @Override
    public int[][] getLoopOffsets() {
        int[][] offsets = {
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };
        return offsets;
    }
}
