package client;

import chess.*;
import exception.ResponseException;
import model.UserData;
import server.Server;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

import java.util.function.Supplier;

public class ClientMain {
    private static Map<String, Supplier<Object>> repls = Map.of(
            "preLogin", PreLoginREPL::new
    );
    private static String activeREPL;
    private static Server server;
    private static final ServerFacade facade = new ServerFacade("http://localhost:8080");
    private static String authToken;


    public static void main(String[] args) {
        setupServer();
        activeREPL = "preLogin";
        startREPL();
    }

    public static void setupServer() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    public static void startREPL() {
        repls.get(activeREPL).get();
    }

    public static String evaluate(String userInput) {
        String[] tokens = userInput.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        var arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch(userInput) {
            case "quit"->quit();
            case "login"->login(arguments);
            case "register"->register(arguments);
            default ->help();
        };
    }

    private static String quit() {
        activeREPL = null;
        return "Exiting CS240 Chess Server!";
    }

    private static String login(String[] arguments) {
        if (arguments.length != 2) {
            return "Unexpected number of login credentials! To login provide a username and password!";
        }
        UserData userData = new UserData(arguments[0], arguments[1], null);
        try {
            facade.login(userData);
        } catch (ResponseException e) {
            return e.toString();
        }
        return "Successfully logged in as " + arguments[0] + "!";
    }

    private static String register(String[] arguments) {
        if (arguments.length != 3) {
            return "Unexpected number of register credentials! To register provide a username, password, and email!";
        }
        UserData userData = new UserData(arguments[0], arguments[1], arguments[2]);
        try {
            facade.register(userData);
        } catch (ResponseException e) {
            return e.toString();
        }
        return "Successfully registered and logged in as " + arguments[0] + "!";
    }

    private static String help() {
        return switch (activeREPL) {
            case "preLogin"->PreLoginREPL.helpText();
            default -> null;
        };
    }
}
