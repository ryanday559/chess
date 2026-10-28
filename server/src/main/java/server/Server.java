package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.GameJoinData;
import model.UserData;
import result.LoginResult;
import service.AuthentificationService;
import service.GameService;
import service.UserService;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private AuthentificationService authService;
    private GameService gameService;
    private UserService userService;
    {
        try {
            authService = new AuthentificationService();
        } catch (DataAccessException e) {
            throw new BadRequestResponse("Cannot Connect to Server!");
        }
        try {
            gameService = new GameService();
        } catch (DataAccessException e) {
            throw new BadRequestResponse("Cannot Connect to Server!");
        }
        try {
            userService = new UserService();
        } catch (DataAccessException e) {
            throw new BadRequestResponse("Cannot Connect to Server!");
        }
    }

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .exception(Exception.class, this::exceptionHandler)
                .exception(DataAccessException.class, this::badRequestExceptionHandler)
                .exception(UnauthorizedException.class, this::unauthorizedExceptionHandler)
                .exception(InUseException.class, this::inUseExceptionHandler);

        javalin.delete("/db", this::clear);
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
    }

    private void clear(Context ctx) throws DataAccessException {
        authService.clear();
        gameService.clear();
        userService.clear();
        ctx.result("{}");
    }

    private void register(Context ctx) throws InUseException, TokenInUseException, DataAccessException {
        UserData userData = getBodyObject(ctx, UserData.class);
        LoginResult loginResult;
        loginResult = userService.register(userData);
        authService.updateAuth(loginResult.authToken(), loginResult.username());
        var result = Map.of("username", loginResult.username(), "authToken", loginResult.authToken());
        ctx.result(new Gson().toJson(result));
    }

    private void login(Context ctx) throws DataAccessException, TokenInUseException, UnauthorizedException {
        UserData userData = getBodyObject(ctx, UserData.class);
        LoginResult loginResult = userService.login(userData);
        authService.updateAuth(loginResult.authToken(), loginResult.username());
        var result = Map.of("username", loginResult.username(), "authToken", loginResult.authToken());
        ctx.result(new Gson().toJson(result));
    }

    private void logout(Context ctx) throws UnauthorizedException, DataAccessException {
        String authToken = ctx.header("authorization");
        authService.removeAuth(authToken);
        ctx.result("{}");
    }

    private void listGames(Context ctx) throws UnauthorizedException, DataAccessException {
        String authToken = ctx.header("authorization");
        authService.authenticate(authToken);
        Collection<GameData> games = gameService.listGames();
        var result = Map.of("games", games);
        ctx.result(new Gson().toJson(result));
    }

    private void createGame(Context ctx) throws UnauthorizedException, DataAccessException {
        String authToken = ctx.header("authorization");
        authService.authenticate(authToken);
        GameData gameData = getBodyObject(ctx, GameData.class);
        int gameID = gameService.createGame(gameData);
        var result = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(result));
    }

    private void joinGame(Context ctx) throws UnauthorizedException, DataAccessException, InUseException {
        GameJoinData gameJoinData = getBodyObject(ctx, GameJoinData.class);
        String authToken = ctx.header("authorization");
        authService.authenticate(authToken);
        AuthData authData = authService.getAuthData(authToken);
        gameService.joinGame(gameJoinData, authData);
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

    private void exceptionHandler(Exception e, Context context) {
        context.status(500);
        writeJsonErrorBody(e, context);
    }

    private void badRequestExceptionHandler(Exception e, Context context) {
        context.status(400);
        writeJsonErrorBody(e, context);
    }

    private void unauthorizedExceptionHandler(Exception e, Context context) {
        context.status(401);
        writeJsonErrorBody(e, context);
    }

    private void inUseExceptionHandler(Exception e, Context context) {
        context.status(403);
        writeJsonErrorBody(e, context);
    }

    private void writeJsonErrorBody(Exception e, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        context.json(body);
    }
}
