package chess;

public class KingMovement implements PieceMovement{
    @Override
    public int[][] getSingleOffsets() {
        int[][] offsets = {
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1},
                {1,0},
                {0,1},
                {-1,0},
                {0,-1}
        };
        return offsets;
    }
}
