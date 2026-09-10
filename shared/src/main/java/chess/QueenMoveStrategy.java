package chess;

class QueenMoveStrategy implements MoveStrategy {
    // We need to check NW, NE, SE, SW, N, S, E, W
    @Override
    public int[][] getLoopMoveOffsets() {
        int[][] loopMoveOffsets = {{1, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        return loopMoveOffsets;
    }
}
