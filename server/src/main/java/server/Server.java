package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.UserData;
import service.UserService;

import java.util.Map;

import static service.AuthentificationService.generateAuthToken;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", ctx->ctx.result("{}"));
        javalin.post("/user", Server::register);
        javalin.post("/session", Server::login);
        javalin.delete("/session", Server::logout);
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
