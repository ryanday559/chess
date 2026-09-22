package chess;

public class KnightMovement implements PieceMovement{
    @Override
    public int[][] getSingleOffsets() {
        int[][] offsets = {
                {2,1},
                {2,-1},
                {-2,1},
                {-2,-1},
                {1,2},
                {1,-2},
                {-1,2},
                {-1,-2}
        };
        return offsets;
    }
}
