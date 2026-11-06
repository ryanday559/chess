package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.GameJoinData;
import model.UserData;
import org.junit.jupiter.api.*;
import result.LoginResult;
import server.Server;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;

    private static final ServerFacade facade = new ServerFacade("http://localhost:8080");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    LoginResult createUser() throws ResponseException {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        return facade.register(user);
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @BeforeEach
    @Test
    public void clearTest() {
        Assertions.assertDoesNotThrow(()->facade.clear());
    }


    @Test
    public void registerTest() {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        Assertions.assertDoesNotThrow(()->facade.register(user));
    }

    @Test
    public void cannotRegisterTest() throws ResponseException {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        facade.register(user);
        Assertions.assertThrows(ResponseException.class, ()->facade.register(user));
    }

    @Test
    public void loginTest() throws ResponseException {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        facade.register(user);
        Assertions.assertDoesNotThrow(()->facade.login(user));
    }

    @Test
    public void badLoginTest() throws ResponseException {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        facade.register(user);
        UserData badLogin = new UserData("bob", "wrong", "bobsemail@email.com");
        Assertions.assertThrows(ResponseException.class, ()->facade.login(badLogin));
    }

    @Test
    public void logoutTest() throws ResponseException {
        LoginResult result = createUser();
        Assertions.assertDoesNotThrow(()->facade.logout(result.authToken()));
    }

    @Test
    public void badAuthLogoutTest() throws ResponseException {
        createUser();
        Assertions.assertThrows(ResponseException.class, ()->facade.logout("bad"));
    }

    @Test
    public void createGameTest() throws ResponseException {
        LoginResult result = createUser();
        GameData gameData = new GameData(-1, null, null, "gameName", null);
        Assertions.assertDoesNotThrow(()->facade.createGame(result.authToken(), gameData));
    }

    @Test
    public void unauthorizedCreateGameTest() throws ResponseException {
        createUser();
        GameData gameData = new GameData(-1, null, null, "gameName", null);
        Assertions.assertThrows(ResponseException.class, ()->facade.createGame("bad", gameData));
    }

    @Test
    public void listGamesTest() throws ResponseException {
        LoginResult result = createUser();
        GameData gameData = new GameData(-1, null, null, "gameName", null);
        facade.createGame(result.authToken(), gameData);
        facade.createGame(result.authToken(), gameData);
        Assertions.assertDoesNotThrow(()->facade.listGames(result.authToken()));
    }

    @Test
    public void unauthorizedListGamesTest() throws ResponseException {
        LoginResult result = createUser();
        GameData gameData = new GameData(-1, null, null, "gameName", null);
        facade.createGame(result.authToken(), gameData);
        facade.createGame(result.authToken(), gameData);
        facade.listGames(result.authToken());
        Assertions.assertThrows(ResponseException.class, ()->facade.listGames("bad"));
    }

    @Test
    public void joinGameTest() throws ResponseException {
        LoginResult result = createUser();
        GameData gameData = new GameData(-1, null, null, "gameName", null);
        facade.createGame(result.authToken(), gameData);
        GameJoinData gameJoinData = new GameJoinData(ChessGame.TeamColor.BLACK, 1);
        Assertions.assertDoesNotThrow(()->facade.joinGame(result.authToken(), gameJoinData));
    }

    @Test
    public void cannotJoinOccupiedTeamTest() throws ResponseException {
        LoginResult result = createUser();
        GameData gameData = new GameData(-1, null, null, "gameName", null);
        facade.createGame(result.authToken(), gameData);
        GameJoinData gameJoinData = new GameJoinData(ChessGame.TeamColor.BLACK, 1);
        facade.joinGame(result.authToken(), gameJoinData);
        facade.logout(result.authToken());
        UserData secondUser = new UserData("sherry", "sherrysPassword", "sherry@email.com");
        LoginResult result2 = facade.register(secondUser);
        Assertions.assertThrows(ResponseException.class, ()->facade.joinGame(result2.authToken(), gameJoinData));

    }
}
