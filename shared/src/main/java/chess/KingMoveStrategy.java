package chess;

class KingMoveStrategy implements MoveStrategy {

    @Override
    public int[][] getSingleMoveOffsets() {
        int[][] singleMoveOffsets = {{1,-1}, {1,1}, {-1,1}, {-1,-1}, {1,0}, {0,1}, {-1,0}, {0,-1}};
        return singleMoveOffsets;
    }

}