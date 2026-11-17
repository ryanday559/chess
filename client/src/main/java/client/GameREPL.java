package client;

import chess.ChessBoard;
import chess.ChessGame;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import service.UserService;

import java.util.Arrays;
import java.util.Scanner;

import static client.ClientMain.authToken;
import static client.ClientMain.quit;
import static client.PostLoginREPL.currentGameID;

public class GameREPL {
    public enum UserStatus {
        OBSERVER,
        WHITEPLAYER,
        BLACKPLAYER
    }
    private WebSocketFacade webSocketFacade;
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
        return "NOT YET IMPLEMENTED";
    }

    private static String move(String[] arguments) {
        return "NOT YET IMPLEMENTED";
    }

    private static String resign() {
        return "NOT YET IMPLEMENTED";
    }

    private static String leave() {
        return "NOT YET IMPLEMENTED";
    }

    private static String playerHelp() {
        return """
                redraw
                highlight <position>
                move <starting position> <end position>
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
