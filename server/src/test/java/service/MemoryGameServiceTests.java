package service;

import chess.ChessGame;
import dataaccess.memory.MemoryGameDataAccess;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryGameServiceTests {
    private MemoryGameDataAccess gameDAO = new MemoryGameDataAccess();

    @Test
    public void clearTest() {
        gameDAO.clear();

        String gameName = "TestGame";
        int gameID = gameDAO.createGame(gameName);

        gameDAO.clear();

        assertNull(gameDAO.getGame(gameID));
    }

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

    @Test
    public void getGamesReturnsAllGames() {
        gameDAO.clear();

        String gameName1 = "TestGame1";
        String gameName2 = "TestGame2";
        int gameID1 = gameDAO.createGame(gameName1);
        int gameID2 = gameDAO.createGame(gameName2);
        GameData game1 = gameDAO.getGame(gameID1);
        GameData game2 = gameDAO.getGame(gameID1);

        Collection<GameData> allGames = gameDAO.getGames();

        assertTrue(allGames.contains(game1));
        assertTrue(allGames.contains(game2));
    }
}
