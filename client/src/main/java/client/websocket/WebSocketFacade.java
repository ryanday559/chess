package client.websocket;

import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import jakarta.websocket.MessageHandler;
import websocket.commands.RequestLegalMovesCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ServerMessage;


import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){
    }

    public void connect(String authToken, Integer gameID) throws ResponseException {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException("Connection Error: Cannot Connect to Server!");
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws ResponseException {
        try {
            UserGameCommand action = new MakeMoveCommand(MakeMoveCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException("Error: Move " + move + " is not a valid move!");
        }
    }

    public void leave(String authToken, Integer gameID) throws ResponseException {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException("Connection Error: Cannot Leave Game!");
        }
    }

    public void highlightMoves(String authToken, Integer gameID, ChessPosition position) throws ResponseException {
        try {
            RequestLegalMovesCommand action = new RequestLegalMovesCommand(
                    UserGameCommand.CommandType.REQUEST_MOVES,
                    authToken,
                    gameID,
                    position
            );
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException("Connection Error: Cannot Leave Game!");
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
}
