package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import io.javalin.websocket.*;
import model.GameData;
import service.AuthentificationService;
import service.GameService;
import service.UserService;
import websocket.commands.RequestLegalMovesCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
                case CONNECT -> connect(ctx.message(), ctx.session);
                case MAKE_MOVE -> makeMove(ctx.message(), ctx.session);
                case LEAVE -> leave(ctx.message(), ctx.session);
                case RESIGN -> resign(ctx.message(), ctx.session);
                case REQUEST_MOVES -> getValidMoves(ctx.message(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String message, Session session) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            Integer gameID = command.getGameID();
            String authToken = command.getAuthToken();
            authService.authenticate(authToken);
            String user = authService.getAuthData(authToken).username();
            connections.add(gameID, session);
            GameData gameData = gameService.getGameData(gameID);
            LoadGameMessage gameBroadcast = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            connections.broadcastJoinMessage(gameData, user, session);
            connections.selfBroadcastGameUpdate(gameBroadcast, session);
        } catch(UnauthorizedException | DataAccessException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.selfBroadcastErrorMessage(errorMessage, session);
        }
    }

    private void makeMove(String message, Session session) throws IOException {
        try {
            MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
            Integer gameID = moveCommand.getGameID();
            String authToken = moveCommand.getAuthToken();
            authService.authenticate(authToken);
            GameData gameData = gameService.getGameData(gameID);
            String user = authService.getAuthData(authToken).username();
            ChessMove move = moveCommand.getMove();
            checkMoveMatchesUserColor(gameData, user, move);
            ChessGame game = gameService.makeMove(gameID, move);
            NotificationMessage moveMessage = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    user + " moved from " + move + "!");
            connections.broadcastToOtherSessionsInGame(gameID, moveMessage, session);
            LoadGameMessage broadcastMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.broadcastGameUpdate(gameID, broadcastMessage);
            sendCheckMessages(gameData, gameID);
            sendCheckmateMessages(gameData, gameID);
        } catch(UnauthorizedException | DataAccessException | ArrayIndexOutOfBoundsException | InvalidMoveException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.selfBroadcastErrorMessage(errorMessage, session);
        }
    }

    private void checkMoveMatchesUserColor(GameData gameData, String user, ChessMove move) throws DataAccessException, InvalidMoveException {
        chess.ChessGame.TeamColor playerColor = getTeamColor(gameData.gameID(), user);
        ChessPosition startPosition = move.getStartPosition();
        if (gameData.game().getBoard().getPiece(startPosition).getTeamColor() != playerColor) {
            throw new InvalidMoveException("Error: Cannot move opposing player's piece!");
        }
    }

    private void sendCheckMessages(GameData gameData, int gameID) throws IOException {
        String checkBaseMessage = " is in check!";
        if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            NotificationMessage checkMessage = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    gameData.whiteUsername() + checkBaseMessage
            );
            connections.broadcastMessage(gameID, checkMessage);
        }
        else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            NotificationMessage checkMessage = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    gameData.blackUsername() + checkBaseMessage
            );
            connections.broadcastMessage(gameID, checkMessage);
        }
    }

    private void sendCheckmateMessages(GameData gameData, int gameID) throws IOException {
        String checkmateBaseMessage = "CHECKMATE! %s wins!";
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            NotificationMessage checkMessage = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format(checkmateBaseMessage, gameData.whiteUsername())
            );
            connections.broadcastMessage(gameID, checkMessage);
        }
        else if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            NotificationMessage checkMessage = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format(checkmateBaseMessage, gameData.blackUsername())
            );
            connections.broadcastMessage(gameID, checkMessage);
        }
    }

    private void leave(String message, Session session) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            Integer gameID = command.getGameID();
            String authToken = command.getAuthToken();
            String user = authService.getAuthData(authToken).username();
            NotificationMessage moveMessage = new NotificationMessage(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    user + " left the game!");
            connections.broadcastToOtherSessionsInGame(gameID, moveMessage, session) ;
            authService.authenticate(authToken);
            ChessGame.TeamColor color = getTeamColor(gameID, user);
            if (color != null) {
                gameService.removePlayer(gameID, color);
            }
            connections.remove(gameID, session);
        } catch(UnauthorizedException | DataAccessException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.selfBroadcastErrorMessage(errorMessage, session);
        }
    }

    ChessGame.TeamColor getTeamColor(int gameID, String user) throws DataAccessException {
        GameData gameData = gameService.getGameData(gameID);
        if (user != null && user.equals(gameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }
        else if (user != null && user.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;
    }

    private void resign(String message, Session session) throws IOException {
    }

    private void getValidMoves(String message, Session session) throws IOException {
        try {
            RequestLegalMovesCommand command = new Gson().fromJson(message, RequestLegalMovesCommand.class);
            Integer gameID = command.getGameID();
            String authToken = command.getAuthToken();
            authService.authenticate(authToken);
            ChessPosition position = command.getPosition();
            ChessGame game = gameService.getGameData(gameID).game();
            Collection<ChessPosition> endPositions = getValidEndPositionsFromPosition(game, position);
            LoadGameMessage broadcastMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, endPositions);
            connections.selfBroadcastGameUpdate(broadcastMessage, session);
        } catch (DataAccessException | UnauthorizedException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.selfBroadcastErrorMessage(errorMessage, session);
        }
    }

    private Collection<ChessPosition> getValidEndPositionsFromPosition(ChessGame game, ChessPosition startPosition) {
        Set<ChessPosition> endPositions = new HashSet<ChessPosition>();
        Collection<ChessMove> moves = game.validMoves(startPosition);
        for (ChessMove move : moves) {
            endPositions.add(move.getEndPosition());
        }
        return endPositions;
    }
}
