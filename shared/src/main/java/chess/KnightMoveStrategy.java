package chess;

class KnightMoveStrategy implements MoveStrategy {
    // We need to check up two over one and up one over two in all NeNwSeSw directions.
    @Override
    public int[][] getSingleMoveOffsets() {
        int[][] singleMoveOffsets = {
                // Ne
                {2, 1}, {1, 2},

                // Nw
                {2, -1}, {1, -2},

                // Se
                {-2, 1}, {-1, 2},

                // Sw
                {-2, -1}, {-1, -2}
        };
        return singleMoveOffsets;
    }
}