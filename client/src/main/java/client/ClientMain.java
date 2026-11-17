package client;

import java.util.Map;
import java.util.function.Supplier;
import client.GameREPL.UserStatus;
import client.websocket.WebSocketFacade;

public class ClientMain {
    public static String activeREPL;
    private final static String URL = "http://localhost:8080";
    public static final ServerFacade FACADE = new ServerFacade(URL);
    public static String authToken;

    private static Map<String, Supplier<Object>> repls = Map.of(
            "preLogin", PreLoginREPL::new,
            "postLogin", PostLoginREPL::new,
            "observeGame", ()-> new GameREPL(UserStatus.OBSERVER, URL),
            "joinGameWhite", ()-> new GameREPL(UserStatus.WHITEPLAYER, URL),
            "joinGameBlack", ()-> new GameREPL(UserStatus.BLACKPLAYER, URL)
    );


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
