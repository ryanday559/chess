package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.GameJoinData;

import java.util.Arrays;
import java.util.Scanner;

import static client.ClientMain.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class PostLoginREPL {
    public PostLoginREPL() {
        repl();
    }

    private void repl() {
        Scanner scanner = new Scanner(System.in);
        String scannerResult = "";
        while (!scannerResult.equals("quit")) {
            System.out.print("\n>>> ");
            try {
                scannerResult = evaluate(scanner.nextLine());
                System.out.print(scannerResult);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
    }

    public static String evaluate(String userInput) {
        String[] tokens = userInput.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        var arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch(command) {
            case "create"->create(arguments);
            case "list"->list();
            case "join"->join(arguments);
            case "observe"->observe(arguments);
            case "logout"->logout();
            case "quit"->quit();
            default ->help();
        };
    }

    private static String create(String[] arguments) {
        if (arguments.length != 1) {
            return makeStringRed("Error: correct format is 'create <GAME NAME>'");
        }
        GameData gameData = new GameData(-1, null, null, arguments[0], null);
        try {
            facade.createGame(authToken, gameData);
        } catch (ResponseException e) {
            return makeStringRed("Error: Could not create game!");
        }
        return "Game " + arguments[0] + " successfully created!";
    }

    private static String list() {
        try {
            GameData[] gamesArray = facade.listGames(authToken);
            return formatGamesArray(gamesArray);
        } catch (ResponseException e) {
            return makeStringRed("Error: Could not list games!");
        }
    }

    private static String formatGamesArray(GameData[] gameData) {
        String formattedGames = "GAMES:";
        for (GameData game : gameData) {
            String whitePlayer = (game.whiteUsername() == null) ? "" : game.whiteUsername();
            String blackPlayer = (game.blackUsername() == null) ? "" : game.blackUsername();
            formattedGames += String.format("\n\t%d. %s WHITE: [%s] BLACK: [%s]", game.gameID(), game.gameName(), whitePlayer, blackPlayer);
        }
        return formattedGames;
    }

    private static String join(String[] arguments) {
        if (arguments.length != 2) {
            return makeStringRed("Error: join requires 2 arguments, [WHITE|BLACK] <GAME ID>");
        }
        ChessGame.TeamColor teamColor;
        if (arguments[1].equals("white")) {
            teamColor = ChessGame.TeamColor.WHITE;
        } else if (arguments[1].equals("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else {
            return makeStringRed("Error: Team color must either be WHITE or BLACK");
        }
        GameJoinData gameJoinData;
        try {
            gameJoinData = new GameJoinData(teamColor, Integer.parseInt(arguments[0]));
        } catch (NumberFormatException e) {
            return makeStringRed("Error: Game Numbers must be integers!");
        }
        GameData game;
        try {
            facade.joinGame(authToken, gameJoinData);
        } catch (ResponseException e) {
            return makeStringRed(e.getMessage());
        }
        System.out.print("Successfully joined game " + arguments[0] + "!");
        activeREPL = switch (teamColor) {
            case BLACK -> "joinGameBlack";
            case WHITE -> "joinGameWhite";
        };
        startREPL();
        return "";
    }

    private static String observe(String[] arguments) {
        if (arguments.length != 1) {
            return makeStringRed("Error: observe requires 1 argument, <GAME Number>");
        }
        int gameID;
        try {
            gameID = Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            return makeStringRed("Error: Game Number must be an integer!");
        }
        GameData game;
        try {
            GameData[] games = facade.listGames(authToken);
            game = findGameById(games, gameID);
        } catch (ResponseException e) {
            return makeStringRed(e.getMessage());
        }
        System.out.print("Successfully observing game " + arguments[0] + "!");
        activeREPL = "observeGame";
        startREPL();
        return "";
    }

    private static GameData findGameById(GameData[] games, Integer gameID) throws ResponseException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new ResponseException("Error: No games with Game Number " + gameID.toString() + " available!");
    }

    private static String logout() {
        try {
            facade.logout(authToken);
        } catch (ResponseException e) {
            return makeStringRed(e.getMessage());
        }
        authToken = null;
        activeREPL = "preLogin";
        startREPL();
        return "Successfully logged out!";
    }

    private static String help() {
        return """
                create <GAME NAME>
                list
                join <Game Number> [WHITE|BLACK]
                observe <Game Number>
                logout
                quit
                help""";
    }

    private static String makeStringRed(String input) {
        return SET_TEXT_COLOR_RED + input + RESET_TEXT_COLOR;
    }
}
