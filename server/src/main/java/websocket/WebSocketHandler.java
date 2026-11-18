package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthentificationDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import io.javalin.websocket.*;
import model.GameData;
import service.AuthentificationService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    // I should probably be calling into the server here that stores the single instances of the services
    // Pass a reference of the services into the handler here. The server will still own them, but there will be a ref here
    private AuthentificationService authService;
    private GameService gameService;
    private UserService userService;

    public WebSocketHandler(AuthentificationService authService, GameService gameService, UserService userService) {
        this.authService = authService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(userGameCommand, ctx.session);
                case MAKE_MOVE -> makeMove(userGameCommand, ctx.session);
                case LEAVE -> leave(userGameCommand, ctx.session);
                case RESIGN -> resign(userGameCommand, ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        try {
            Integer gameID = command.getGameID();
            String authToken = command.getAuthToken();
            authService.authenticate(authToken);
            String user = authService.getAuthData(authToken).username();
            connections.add(gameID, session);
            GameData gameData = gameService.getGameData(gameID);
            LoadGameMessage gameBroadcast = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            connections.broadcastJoinMessage(gameData, user);
            connections.selfBroadcastGameUpdate(gameBroadcast, session);
        } catch(UnauthorizedException | DataAccessException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void makeMove(UserGameCommand command, Session session) throws IOException {
        try {
            MakeMoveCommand moveCommand = (MakeMoveCommand) command;
            Integer gameID = moveCommand.getGameID();
            String authToken = moveCommand.getAuthToken();
            authService.authenticate(authToken);
            ChessMove move = moveCommand.getMove();
            ChessGame game = gameService.makeMove(gameID, move);
            LoadGameMessage broadcastMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.broadcastGameUpdate(gameID, broadcastMessage);
        } catch(UnauthorizedException | DataAccessException | InvalidMoveException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void leave(UserGameCommand command, Session session) throws IOException {
        try {
            Integer gameID = command.getGameID();
            String authToken = command.getAuthToken();
            authService.authenticate(authToken);
            connections.remove(gameID, session);
        } catch(UnauthorizedException | DataAccessException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
    }
}
