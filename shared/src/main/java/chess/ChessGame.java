package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        board = new ChessBoard();
        board.resetBoard();
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


    private Collection<ChessPosition> getRookCastlingPartnerPosition(ChessPosition rookPosition) {
        Collection<ChessPosition> kingCastlePosition = new ArrayList<ChessPosition>();
        int rookRow = rookPosition.getRow();
        boolean isRightRook = false;
        if (rookPosition.getColumn() == 8) {
            isRightRook = true;
        }
        ChessPosition potentialKingPosition = new ChessPosition(rookRow, 5);
        ChessPiece potentialKingPiece = board.getPiece(potentialKingPosition);
        if (
            isRightRook &&
            potentialKingPiece.getPieceType() == ChessPiece.PieceType.KING &&
            !potentialKingPiece.checkHasMoved() &&
            board.checkEmptyColumnsBetweenPiecesInRow(potentialKingPosition, rookPosition)
        ) {
            kingCastlePosition.add(potentialKingPosition);
        }
        else if (
            potentialKingPiece.getPieceType() == ChessPiece.PieceType.KING &&
            !potentialKingPiece.checkHasMoved() &&
            board.checkEmptyColumnsBetweenPiecesInRow(rookPosition, potentialKingPosition)
        ) {
            kingCastlePosition.add(potentialKingPosition);
        }
        return kingCastlePosition;
    }


    private Collection<ChessPosition> getKingCastlingPartnersPositions(ChessPosition kingPosition) {
        Collection<ChessPosition> partnersPositions = new ArrayList<ChessPosition>();
        int kingRow = kingPosition.getRow();
        ChessPosition potentialRookLocationLeft = new ChessPosition(kingRow, 1);
        ChessPosition potentialRookLocationRight = new ChessPosition(kingRow, 8);
        ChessPiece potentialRookLeft = board.getPiece(potentialRookLocationLeft);
        ChessPiece potentialRookRight = board.getPiece(potentialRookLocationRight);
        if (
            potentialRookLeft.getPieceType() == ChessPiece.PieceType.ROOK &&
            !potentialRookLeft.checkHasMoved() &&
            board.checkEmptyColumnsBetweenPiecesInRow(potentialRookLocationLeft, kingPosition)
        ) {
            partnersPositions.add(potentialRookLocationLeft);
        }
        if (
            potentialRookRight.getPieceType() == ChessPiece.PieceType.ROOK &&
            !potentialRookRight.checkHasMoved() &&
            board.checkEmptyColumnsBetweenPiecesInRow(kingPosition, potentialRookLocationRight)
        ) {
            partnersPositions.add(potentialRookLocationRight);
        }
        return partnersPositions;
    }


    private Collection<ChessMove> addKingCastleMoves(ChessPosition kingPosition, Collection<ChessPosition> rookPositions) {
        Collection<ChessMove> kingCastleMoves = new ArrayList<ChessMove>();
        int kingRow = kingPosition.getRow();
        int kingColumn = kingPosition.getColumn();
        for (ChessPosition rookPosition : rookPositions) {
            int rookColumn = rookPosition.getColumn();
            ChessPosition kingEndPosition;
            if (rookColumn < kingColumn) {
                kingEndPosition = new ChessPosition(kingRow, 3);
            }
            else {
                kingEndPosition = new ChessPosition(kingRow, 7);
            }
            kingCastleMoves.add(new ChessMove(kingPosition, kingEndPosition, null));
        }
        return kingCastleMoves;
    }


    private Collection<ChessMove> addRookCastleMove(ChessPosition rookPosition, Collection<ChessPosition> kingPosition) {
        Collection<ChessMove> rookCastleMove = new ArrayList<ChessMove>();
        if (kingPosition.isEmpty()) {
            return rookCastleMove;
        }
        int rookRow = rookPosition.getRow();
        ChessPosition rookEndPosition;
        if (rookRow == 8) {
            rookEndPosition = new ChessPosition(rookRow, 6);
        }
        else {
            rookEndPosition = new ChessPosition(rookRow, 4);
        }
        rookCastleMove.add(new ChessMove(rookPosition, rookEndPosition, null));
        return rookCastleMove;
    }


    private Collection<ChessMove> addPotentialCastleMoves(ChessPosition startPosition) {
        ChessPiece pieceToMove = board.getPiece(startPosition);
        ChessPiece.PieceType pieceType = pieceToMove.getPieceType();
        if (pieceType == ChessPiece.PieceType.KING && !pieceToMove.checkHasMoved()) {
            Collection<ChessPosition> castlingPartnersPositions = getKingCastlingPartnersPositions(startPosition);
            return addKingCastleMoves(startPosition, castlingPartnersPositions);
        }
        else if (pieceType == ChessPiece.PieceType.ROOK && !pieceToMove.checkHasMoved()) {
            Collection<ChessPosition> castlingPartnersPositions = getRookCastlingPartnerPosition(startPosition);\
            return addRookCastleMove(startPosition, castlingPartnersPositions);
        }
        return new ArrayList<ChessMove>();
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
        validMoveList = addPotentialCastleMoves(startPosition);
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
        ChessPosition endPosition = move.getEndPosition();
        if (!moveContainsPiece(move)) {
            throw new InvalidMoveException("Move " + move + "is invalid!");
        }
        Collection<ChessMove> possibleMoves = validMoves(startPosition);
        if (!possibleMoves.contains(move) || !isMoveTurn(move)) {
            throw new InvalidMoveException("Move " + move + "is invalid!");
        }
        board.movePiece(move);
        board.getPiece(endPosition).setHasMoved(true);
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


    private Collection<ChessMove> getAllTeamValidMoves(TeamColor team) {
        Collection<ChessPosition> teamPiecePositions = board.getTeamPiecePositions(team);
        Collection<ChessMove> allTeamValidMoves = new ArrayList<ChessMove>();
        for (ChessPosition position : teamPiecePositions) {
            Collection<ChessMove> pieceValidMoves = validMoves(position);
            allTeamValidMoves.addAll(pieceValidMoves);
        }
        return allTeamValidMoves;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> teamValidMoves = getAllTeamValidMoves(teamColor);
        if (!isInCheck(teamColor) && teamValidMoves.isEmpty()) {
            return true;
        }
        return false;
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


    private boolean checkEqualGame(ChessGame otherGame) {
        if (getBoard().equals(otherGame.getBoard()) && getTeamTurn().equals(otherGame.getTeamTurn())) {
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj != null && obj.getClass() != getClass()) {
            return false;
        }
        ChessGame that = (ChessGame) obj;
        return checkEqualGame(that);
    }


    @Override
    public int hashCode() {
        return 31 * Objects.hash(getBoard(), getTeamTurn());
    }
}
