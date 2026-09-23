package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn = TeamColor.WHITE;
    private ChessBoard board;


    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }


    private void changeCurrentTurn() {
        TeamColor opposingTeam = getOpposingTeam(getTeamTurn());
        setTeamTurn(opposingTeam);
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard originalBoard = getBoard();
        ChessPiece movingPiece = board.getPiece(startPosition);
        TeamColor pieceColor = movingPiece.getTeamColor();
        Collection<ChessMove> possibleMoves = movingPiece.pieceMoves(getBoard(), startPosition);
        Collection<ChessMove> validMoveList = new ArrayList<ChessMove>();
        for (ChessMove move : possibleMoves) {
            ChessBoard boardCopy = new ChessBoard(originalBoard);
            boardCopy.movePiece(move);
            setBoard(boardCopy);
            if (!isInCheck(pieceColor)) {
                validMoveList.add(move);
            }
        }
        setBoard(originalBoard);
        return validMoveList;
    }


    private boolean isMoveTurn(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        TeamColor pieceTeam = piece.getTeamColor();
        if (pieceTeam == getTeamTurn()) {
            return true;
        }
        return false;
    }


    private boolean moveContainsPiece(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            return false;
        }
        return true;
    }


    private void doPromotion(ChessMove move) {
        ChessPiece.PieceType promotionType = move.getPromotionPiece();
        if (promotionType == null) {
            return;
        }
        ChessPosition promotionPosition = move.getEndPosition();
        TeamColor promotionColor = board.getPiece(promotionPosition).getTeamColor();
        board.addPiece(promotionPosition, new ChessPiece(promotionColor, promotionType));
    }


    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        if (!moveContainsPiece(move)) {
            throw new InvalidMoveException("Move " + move + "is invalid!");
        }
        Collection<ChessMove> possibleMoves = validMoves(startPosition);
        if (!possibleMoves.contains(move) || !isMoveTurn(move)) {
            throw new InvalidMoveException("Move " + move + "is invalid!");
        }
        board.movePiece(move);
        changeCurrentTurn();
        doPromotion(move);
    }


    private Collection<ChessPosition> getMoveCollectionEndPositions(Collection<ChessMove> moves) {
        Collection<ChessPosition> endPositions = new ArrayList<ChessPosition>();
        for (ChessMove move : moves) {
            endPositions.add(move.getEndPosition());
        }
        return endPositions;
    }


    private TeamColor getOpposingTeam(TeamColor teamColor) {
        TeamColor opposingTeam;
        if (teamColor == TeamColor.WHITE) {
            opposingTeam = TeamColor.BLACK;
        }
        else {
            opposingTeam = TeamColor.WHITE;
        }
        return opposingTeam;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor opposingTeam = getOpposingTeam(teamColor);
        Collection<ChessMove> otherTeamMoves = board.getAllTeamMovePossibilities(opposingTeam);
        Collection<ChessPosition> otherTeamEndPositions = getMoveCollectionEndPositions(otherTeamMoves);
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        if (otherTeamEndPositions.contains(kingPosition)) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessBoard currentBoard = getBoard();
        Collection<ChessMove> allPossibleMoves = currentBoard.getAllTeamMovePossibilities(teamColor);
        for (ChessMove move : allPossibleMoves) {
            board = new ChessBoard(currentBoard);
            board.movePiece(move);
            if (!isInCheck(teamColor)) {
                setBoard(currentBoard);
                return false;
            }
        }
        setBoard(currentBoard);
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return new ChessBoard(board);
    }
}
