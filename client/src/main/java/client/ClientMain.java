package client;

import chess.*;
import java.util.Map;
import java.util.function.Supplier;
import client.GameREPL.UserStatus;

public class ClientMain {
    private static Map<String, Supplier<Object>> repls = Map.of(
            "preLogin", PreLoginREPL::new,
            "postLogin", PostLoginREPL::new,
            "observeGame", ()-> new GameREPL(UserStatus.OBSERVER),
            "joinGameWhite", ()-> new GameREPL(UserStatus.WHITEPLAYER),
            "joinGameBlack", ()-> new GameREPL(UserStatus.BLACKPLAYER)
    );
    public static String activeREPL;
    public static final ServerFacade facade = new ServerFacade("http://localhost:8080");
    public static String authToken;


    public static void main(String[] args) {
        activeREPL = "preLogin";
        startREPL();
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
