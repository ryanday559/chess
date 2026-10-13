package service;

import dataaccess.memory.MemoryGameDataAccess;
import model.GameData;

import java.util.Collection;

public class GameService {
    private MemoryGameDataAccess gameDAO = new MemoryGameDataAccess();

    public GameService(){}

    public void clear() {
        gameDAO.clear();
    }

    public Collection<GameData> listGames() {
        return null;
    }
}
