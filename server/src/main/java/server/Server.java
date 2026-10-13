package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.GameData;
import model.UserData;
import service.UserService;

import java.util.Map;

import static service.AuthentificationService.generateAuthToken;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", Server::clear);
        javalin.post("/user", Server::register);
        javalin.post("/session", Server::login);
        javalin.delete("/session", Server::logout);
        javalin.get("/game", Server::listGames);
        javalin.post("/game", Server::createGame);
        javalin.put("/game", Server::joinGame);
    }

    private static void clear(Context ctx) {
        ctx.result("{}");
    }

    private static void register(Context ctx) {
        String authToken = generateAuthToken();
        UserData userData = getBodyObject(ctx, UserData.class);
        var result = Map.of("username", userData.username(), "authToken", authToken);
        ctx.result(new Gson().toJson(result));
    }

    private static void login(Context ctx) {
        UserData userData = getBodyObject(ctx, UserData.class);
        var result = Map.of("username", userData.username(), "password", userData.password());
        ctx.result(new Gson().toJson(result));
    }

    private static void logout(Context ctx) {
        // Need to add in an authtoken check
        ctx.result("{}");
    }

    private static void listGames(Context ctx) {
        // list games logic here (no body)
        var result = Map.of("games", "");
        ctx.result(new Gson().toJson(result));
    }

    private static void createGame(Context ctx) {
        GameData gameData = getBodyObject(ctx, GameData.class);
        var result = Map.of("gameName", gameData.gameName());
        ctx.result(new Gson().toJson(result));
    }

    private static void joinGame(Context ctx) {
        ctx.result("{}");
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private static <T> T getBodyObject(Context context, Class<T> clazz) {
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
