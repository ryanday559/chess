package client;

import chess.*;
import client.websocket.WebSocketFacade;
import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static client.ClientMain.*;
import static client.PostLoginREPL.currentGameID;
import static client.PostLoginREPL.makeStringRed;

public class GameREPL {
    public enum UserStatus {
        OBSERVER,
        WHITEPLAYER,
        BLACKPLAYER
    }
    private static WebSocketFacade webSocketFacade;
    private String url;
    private static UserStatus userStatus;

    public GameREPL(UserStatus userStatus, String url) {
        this.url = url;
        this.userStatus = userStatus;
        try {
            switch (userStatus) {
                case WHITEPLAYER -> whitePlayerConnection();
                case BLACKPLAYER -> blackPlayerConnection();
                case OBSERVER -> observerConnection();
            }
        } catch (ResponseException ex) {
            System.out.print("Error: Cannot Connect to Server!");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String scannerResult = "";
        while (!scannerResult.equals("leave")) {
            System.out.print("\n>>> ");
            try {
                scannerResult = evaluate(scanner.nextLine());
                System.out.print(scannerResult);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private void observerConnection() throws ResponseException {
        webSocketFacade = new WebSocketFacade(url, new WhitePlayerHandler());
        webSocketFacade.connect(authToken, currentGameID);
    }

    private void whitePlayerConnection() throws ResponseException {
        webSocketFacade = new WebSocketFacade(url, new WhitePlayerHandler());
        webSocketFacade.connect(authToken, currentGameID);
    }

    private void blackPlayerConnection() throws ResponseException {
        webSocketFacade = new WebSocketFacade(url, new BlackPlayerHandler());
        webSocketFacade.connect(authToken, currentGameID);
    }

    public static String evaluate(String userInput) {
        String[] tokens = userInput.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        var arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch(command) {
            case "redraw"->redraw();
            case "highlight"->highlight(arguments);
            case "move"->move(arguments);
            case "resign"->resign();
            case "leave"->leave();
            default ->help();
        };
    }

    private static String help() {
        return switch (userStatus) {
            case BLACKPLAYER -> playerHelp();
            case WHITEPLAYER -> playerHelp();
            case OBSERVER -> observerHelp();
        };
    }

    private static String redraw() {
        return "NOT YET IMPLEMENTED";
    }

    private static String highlight(String[] arguments) {
        if (arguments.length != 1) {
            return makeStringRed("Error: Highlight expects a chess position!");
        }
        try {
            ChessPosition position = getChessPositionFromPositionString(arguments[0]);
            webSocketFacade.highlightMoves(authToken, currentGameID, position);
        } catch (ResponseException ex) {
            return makeStringRed(ex.getMessage());
        }
        return "";
    }

    private static String move(String[] arguments) {
        ChessMove move;
        try {
            move = parseChessMoveFromUserMoveInputs(arguments);
            webSocketFacade.makeMove(authToken, currentGameID, move);
        } catch (ResponseException ex) {
            return makeStringRed(ex.getMessage());
        }
        return "";
    }

    private static ChessMove parseChessMoveFromUserMoveInputs(String[] arguments) throws ResponseException {
        if (arguments.length < 2 || arguments.length > 3) {
            throw new ResponseException("Error: Move must fit the format 'move <starting position> <end position> <promotion if applicable - Q | K | N | R | B >'");
        }
        ChessPosition firstPosition = getChessPositionFromPositionString(arguments[0]);
        ChessPosition secondPosition = getChessPositionFromPositionString(arguments[1]);
        ChessPiece.PieceType promotionType = null;
        if (arguments.length == 3) {
            String thirdArg = arguments[2].toLowerCase();
            promotionType = switch (thirdArg) {
                case "q" -> ChessPiece.PieceType.QUEEN;
                case "k" -> ChessPiece.PieceType.KING;
                case "n" -> ChessPiece.PieceType.KNIGHT;
                case "r" -> ChessPiece.PieceType.ROOK;
                case "b" -> ChessPiece.PieceType.BISHOP;
                default -> throw new ResponseException("Error: Unknown promotion piece " + arguments[2]);
            };
        }
        return new ChessMove(firstPosition, secondPosition, promotionType);
    }

    private static ChessPosition getChessPositionFromPositionString(String positionString) throws ResponseException {
        if (positionString.length() != 2) {
            throw new ResponseException(String.format("Error: Invalid Position Format: '%s'. Should be [a-h][1-8]", positionString));
        }
        String lowerPositionString = positionString.toLowerCase();
        int positionCol = (int) lowerPositionString.charAt(0) - 96;
        int positionRow = Character.getNumericValue(lowerPositionString.charAt(1));
        return new ChessPosition(positionRow, positionCol);
    }

    private static String resign() {
        try {
            webSocketFacade.resign(authToken, currentGameID);
        } catch (ResponseException ex) {
            return makeStringRed(ex.getMessage());
        }
        return "";
    }

    private static String leave() {
        try {
            webSocketFacade.leave(authToken, currentGameID);
        } catch (ResponseException ex) {
            return makeStringRed(ex.getMessage());
        }
        System.out.print("Left the game!");
        currentGameID = null;
        activeREPL = "postLogin";
        startREPL();
        return "";
    }

    private static String playerHelp() {
        return """
                redraw
                highlight <position>
                move <starting position> <end position> <promotion if applicable - Q | K | N | R | B >
                resign
                leave
                help""";
    }

    private static String observerHelp() {
        return """
                redraw
                highlight <position>
                leave
                help""";
    }

}
