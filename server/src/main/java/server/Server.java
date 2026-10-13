package server;

import com.google.gson.Gson;
import handler.AuthentificationHandler;
import handler.GameHandler;
import handler.UserHandler;
import io.javalin.*;
import io.javalin.http.Context;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.AuthentificationService;
import service.GameService;
import service.UserService;

import java.util.Map;

import static service.AuthentificationService.generateAuthToken;

public class Server {

    private final Javalin javalin;
    private AuthentificationService authService = new AuthentificationService();
    private GameService gameService = new GameService();
    private UserService userService = new UserService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::clear);
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
    }

    private void clear(Context ctx) {
        authService.clear();
        gameService.clear();
        userService.clear();
        ctx.result("{}");
    }

    private void register(Context ctx) {
        String authToken = generateAuthToken();
        UserData userData = getBodyObject(ctx, UserData.class);
        var result = Map.of("username", userData.username(), "authToken", authToken);
        ctx.result(new Gson().toJson(result));
    }

    private void login(Context ctx) {
        UserData userData = getBodyObject(ctx, UserData.class);
        var result = Map.of("username", userData.username(), "password", userData.password());
        ctx.result(new Gson().toJson(result));
    }

    private void logout(Context ctx) {
        // Need to add in an authtoken check
        ctx.result("{}");
    }

    private void listGames(Context ctx) {
        // list games logic here (no body)
        var result = Map.of("games", "");
        ctx.result(new Gson().toJson(result));
    }

    private void createGame(Context ctx) {
        GameData gameData = getBodyObject(ctx, GameData.class);
        var result = Map.of("gameName", gameData.gameName());
        ctx.result(new Gson().toJson(result));
    }

    private void joinGame(Context ctx) {
        ctx.result("{}");
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private <T> T getBodyObject(Context context, Class<T> clazz) {
        var bodyObject = new Gson().fromJson(context.body(), clazz);
        if (bodyObject == null) {
            throw new RuntimeException("missing required body");
        }
        return bodyObject;
    }

    public void stop() {
        javalin.stop();
    }
}
