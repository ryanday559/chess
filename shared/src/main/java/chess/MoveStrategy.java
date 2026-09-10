package chess;
import java.util.List;

interface MoveStrategy {
    List<ChessPosition> getValidMoves(ChessPosition position, ChessBoard board);
}
