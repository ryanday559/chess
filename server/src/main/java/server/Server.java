package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.TokenInUseException;
import dataaccess.UsernameInUseException;
import io.javalin.*;
import io.javalin.http.Context;
import model.GameData;
import model.UserData;
import result.LoginResult;
import service.AuthentificationService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Map;

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

    private void register(Context ctx) throws UsernameInUseException, TokenInUseException {
        UserData userData = getBodyObject(ctx, UserData.class);
        LoginResult loginResult = userService.register(userData);
        authService.updateAuth(loginResult.authToken(), loginResult.username());
        var result = Map.of("username", loginResult.username(), "authToken", loginResult.authToken());
        ctx.result(new Gson().toJson(result));
    }

    private void login(Context ctx) throws DataAccessException, TokenInUseException {
        UserData userData = getBodyObject(ctx, UserData.class);
        LoginResult loginResult = userService.login(userData);
        authService.updateAuth(loginResult.authToken(), loginResult.username());
        var result = Map.of("username", loginResult.username(), "authToken", loginResult.authToken());
        ctx.result(new Gson().toJson(result));
    }

    private void logout(Context ctx) {
        String authToken = ctx.header("authorization");
        authService.removeAuth(authToken);
        ctx.result("{}");
    }

    private void listGames(Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            authService.authenticate(authToken);
        }
        catch (DataAccessException e) {
            ctx.result(new Gson().toJson(e.getMessage()));
            return;
        }
        Collection<GameData> games = gameService.listGames();
        var result = Map.of("games", authToken);
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
