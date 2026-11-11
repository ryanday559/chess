package client;

import exception.ResponseException;
import model.UserData;

import java.util.Arrays;
import java.util.Scanner;

import static client.ClientMain.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class PreLoginREPL {

    public PreLoginREPL() {
        repl();
    }

    private void repl() {
        System.out.println("♕ 240 Chess Client. Type help to get started: ");
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>> ");
            try {
                result = evaluate(scanner.nextLine());
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }
    }

    public static String evaluate(String userInput) {
        String[] tokens = userInput.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        var arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch(command) {
            case "quit"->quit();
            case "login"->login(arguments);
            case "register"->register(arguments);
            default ->help();
        };
    }

    private static String login(String[] arguments) {
        if (arguments.length != 2) {
            return SET_TEXT_COLOR_RED
                    + "ERROR: Unexpected number of login credentials! To login provide a username and password!"
                    + RESET_TEXT_COLOR;
        }
        UserData userData = new UserData(arguments[0], arguments[1], null);
        try {
            authToken = facade.login(userData).authToken();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR;
        }
        activeREPL = "postLogin";
        System.out.print("Successfully logged in as " + arguments[0] + "! Type help for new options.");
        startREPL();
        return "";
    }

    private static String register(String[] arguments) {
        if (arguments.length != 3) {
            return SET_TEXT_COLOR_RED
                    + "Error: Unexpected number of register credentials! To register provide a username, password, and email!"
                    + RESET_TEXT_COLOR;
        }
        UserData userData = new UserData(arguments[0], arguments[1], arguments[2]);
        try {
            authToken = facade.register(userData).authToken();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + e.getMessage() + RESET_TEXT_COLOR;
        }
        activeREPL = "postLogin";
        System.out.print("Successfully registered and logged in as " + arguments[0] + "! Type help for new options.");
        startREPL();
        return "";
    }

    public static String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL>
                login <USERNAME> <PASSWORD>
                quit
                help""";
    }

}
