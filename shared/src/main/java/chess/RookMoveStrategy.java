package chess;

class RookMoveStrategy implements MoveStrategy {

    @Override
    public int[][] getLoopMoveOffsets() {
        int[][] loopMoveOffsets = {{1,0}, {0,1}, {-1,0}, {0,-1}};
        return loopMoveOffsets;
    }

}