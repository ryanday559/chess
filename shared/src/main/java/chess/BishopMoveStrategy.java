package chess;


class BishopMoveStrategy implements MoveStrategy {
    // We need to check NW, NE, SE, and SW
    @Override
    int[][] loopMoveOffsets = {{1,-1}, {1,1}, {-1,1}, {-1,-1}};
}
