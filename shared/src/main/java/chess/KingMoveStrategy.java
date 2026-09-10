package chess;

class KingMoveStrategy implements MoveStrategy {
    // We need to check NW, NE, SE, SW, N, S, E, W
    @Override
    public int[][] getSingleMoveOffsets() {
        int[][] singleMoveOffsets = {{1,-1}, {1,1}, {-1,1}, {-1,-1}, {1,0}, {0,1}, {-1,0}, {0,-1}};
        return singleMoveOffsets;
    }
}