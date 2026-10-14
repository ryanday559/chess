package service;

import dataaccess.DataAccessException;
import dataaccess.InUseException;
import dataaccess.memory.MemoryGameDataAccess;
import model.AuthData;
import model.GameData;
import model.GameJoinData;

import java.util.Collection;

public class GameService {
    private MemoryGameDataAccess gameDAO = new MemoryGameDataAccess();

    public GameService(){}

    public void clear() {
        gameDAO.clear();
    }

    public Collection<GameData> listGames() {
        return gameDAO.getGames();
    }

    public int createGame(GameData gameData) throws DataAccessException {
        return gameDAO.createGame(gameData.gameName());
    }

    public void joinGame(GameJoinData gameJoinData, AuthData authData) throws DataAccessException, InUseException {
        gameDAO.updatePlayerColor(gameJoinData.playerColor(), authData.username(), gameJoinData.gameID());
    }
}
