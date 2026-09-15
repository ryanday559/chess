package chess;


class BishopMoveStrategy implements MoveStrategy {

    @Override
    public int[][] getLoopMoveOffsets() {
        int[][] loopMoveOffsets = {{1,-1}, {1,1}, {-1,1}, {-1,-1}};
        return loopMoveOffsets;
    };

}
