package chess;

public class QueenMovement implements PieceMovement{
    @Override
    public int[][] getLoopOffsets() {
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
