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
            "preLogin", PreLoginREPL::new,
            "postLogin", PostLoginREPL::new
    );
    public static String activeREPL;
    private static Server server;
    public static final ServerFacade facade = new ServerFacade("http://localhost:8080");
    public static String authToken;


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

    public static String quit() {
        activeREPL = null;
        System.out.print("Exiting CS240 Chess Server!");
        System.exit(0);
        return "";
    }


}
