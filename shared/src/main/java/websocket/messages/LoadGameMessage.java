package websocket.messages;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Set;

public class LoadGameMessage extends ServerMessage {
    private ChessGame game;
    private Collection<ChessPosition> highlightPositions = Set.of();

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public LoadGameMessage(ServerMessageType type, ChessGame game, Collection<ChessPosition> highlightPositions) {
        super(type);
        this.game = game;
        this.highlightPositions = highlightPositions;
    }

    public ChessGame getGame() {
        return game;
    }

    public Collection<ChessPosition> getHighlightPositions() {
        return highlightPositions;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
