package websocket.commands;

import chess.ChessPosition;

public class RequestLegalMovesCommand extends UserGameCommand{
    private ChessPosition position;

    public RequestLegalMovesCommand(CommandType commandType, String authToken, Integer gameID, ChessPosition position) {
        super(commandType, authToken, gameID);
        this.position = position;
    }

    public ChessPosition getPosition() {
        return position;
    }
}