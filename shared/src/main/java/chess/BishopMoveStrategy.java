package chess;


class BishopMoveStrategy implements MoveStrategy {
    // We need to check NW, NE, SE, and SW
    @Override
    public int[][] getLoopMoveOffsets() {
        int[][] loopMoveOffsets = {{1,-1}, {1,1}, {-1,1}, {-1,-1}};
        return loopMoveOffsets;
    };
}
