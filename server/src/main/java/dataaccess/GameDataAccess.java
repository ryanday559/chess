package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public Collection<GameData> getGames();
    public int createGame(String gameName);
    public GameData getGame(int gameID);
    public void updatePlayerColor(ChessGame.TeamColor playerColor, GameData gameData);
    public void clear();
}
