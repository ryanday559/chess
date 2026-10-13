package dataaccess;

import chess.ChessGame;
import model.GameData;

public interface GameDataAccess {
    public GameData[] getGames();
    public int createGame(String gameName);
    public GameData getGame(int gameID);
    public void updatePlayerColor(ChessGame.TeamColor playerColor, GameData gameData);
    public void clear();
}
