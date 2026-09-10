package chess;

class RookMoveStrategy implements MoveStrategy {
    // N, S, E, W
    @Override
    public int[][] getLoopMoveOffsets() {
        int[][] loopMoveOffsets = {{1,0}, {0,1}, {-1,0}, {0,-1}};
        return loopMoveOffsets;
    }
}