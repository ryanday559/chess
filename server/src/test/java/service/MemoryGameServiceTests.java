package service;

import chess.ChessGame;
import dataaccess.memory.MemoryGameDataAccess;
import model.GameData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryGameServiceTests {
    private MemoryGameDataAccess gameDAO = new MemoryGameDataAccess();

    @Test
    public void createGetGameTest() {
        gameDAO.clear();

        String gameName = "TestGame";
        int gameID = gameDAO.createGame(gameName);
        GameData gameData = gameDAO.getGame(gameID);

        assertEquals(gameID, gameData.gameID());
        assertEquals(gameName, gameData.gameName());
        assertEquals(ChessGame.class, gameData.game().getClass());
    }
}
