package service;

import dataaccess.DataAccessException;
import dataaccess.InUseException;
import dataaccess.memory.MemoryGameDataAccess;
import dataaccess.mysql.MySqlGameDataAccess;
import model.AuthData;
import model.GameData;
import model.GameJoinData;

import java.util.Collection;

public class GameService {
    private MySqlGameDataAccess gameDAO = new MySqlGameDataAccess();

    public GameService() throws DataAccessException {}

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return gameDAO.getGames();
    }

    public int createGame(GameData gameData) throws DataAccessException {
        return gameDAO.createGame(gameData.gameName());
    }

    public void joinGame(GameJoinData gameJoinData, AuthData authData) throws DataAccessException, InUseException {
        gameDAO.updatePlayerColor(gameJoinData.playerColor(), authData.username(), gameJoinData.gameID());
    }
}
