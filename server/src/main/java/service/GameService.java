package service;

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

    public int createGame(GameData gameData) {
        return gameDAO.createGame(gameData.gameName());
    }

    public void joinGame(GameJoinData gameJoinData, AuthData authData) {
        gameDAO.updatePlayerColor(gameJoinData.color(), authData.username(), gameJoinData.gameID());
    }
}
