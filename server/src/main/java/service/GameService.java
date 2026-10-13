package service;

import dataaccess.memory.MemoryGameDataAccess;

public class GameService {
    private MemoryGameDataAccess gameDAO = new MemoryGameDataAccess();

    public GameService(){}

    public void clear() {
        gameDAO.clear();
    }
}
