package client;

import exception.ResponseException;
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
    public void loginTest() throws IOException, InterruptedException, ResponseException {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        facade.register(user);
        Assertions.assertDoesNotThrow(()->facade.login(user));
    }

    @Test
    public void logoutTest() throws ResponseException {
        UserData user = new UserData("bob", "bobsPassword", "bobsemail@email.com");
        LoginResult result = facade.register(user);
        Assertions.assertDoesNotThrow(()->facade.logout(result.authToken()));
    }

}
