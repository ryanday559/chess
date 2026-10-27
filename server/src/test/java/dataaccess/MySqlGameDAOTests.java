package dataaccess;

import chess.ChessGame;
import dataaccess.memory.MemoryGameDataAccess;
import dataaccess.mysql.MySqlGameDataAccess;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlGameDAOTests {
    private MySqlGameDataAccess gameDAO = new MySqlGameDataAccess();

    public MySqlGameDAOTests() throws DataAccessException {
    }

    @Test
    public void clearTest() throws DataAccessException {
        gameDAO.clear();

        String gameName = "TestGame";
        int gameID = gameDAO.createGame(gameName);

        gameDAO.clear();

        assertNull(gameDAO.getGame(gameID));
    }

    @Test
    public void createGetGameTest() throws DataAccessException {
        gameDAO.clear();

        String gameName = "TestGame";
        int gameID = gameDAO.createGame(gameName);
        GameData gameData = gameDAO.getGame(gameID);

        assertEquals(gameID, gameData.gameID());
        assertEquals(gameName, gameData.gameName());
        assertEquals(ChessGame.class, gameData.game().getClass());
    }

    @Test
    public void getGamesReturnsAllGames() throws DataAccessException {
        gameDAO.clear();

        String gameName1 = "TestGame1";
        String gameName2 = "TestGame2";
        int gameID1 = gameDAO.createGame(gameName1);
        int gameID2 = gameDAO.createGame(gameName2);
        GameData game1 = gameDAO.getGame(gameID1);
        GameData game2 = gameDAO.getGame(gameID2);

        Collection<GameData> allGames = gameDAO.getGames();

        assertTrue(allGames.contains(game1));
        assertTrue(allGames.contains(game2));
    }

    @Test
    public void updateBlackPlayer() throws DataAccessException, UnauthorizedException, InUseException {
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.BLACK, playerName, gameID);

        assertEquals(playerName, gameDAO.getGame(gameID).blackUsername());
    }

    @Test
    public void updateBlackPlayerDoesNotAffectWhitePlayer() throws DataAccessException, UnauthorizedException, InUseException {
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.BLACK, playerName, gameID);

        assertNull(gameDAO.getGame(gameID).whiteUsername());
    }

    @Test
    public void updateWhitePlayer() throws DataAccessException, UnauthorizedException, InUseException {
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.WHITE, playerName, gameID);

        assertEquals(playerName, gameDAO.getGame(gameID).whiteUsername());
    }

    @Test
    public void updateWhitePlayerDoesNotAffectBlackPlayer() throws DataAccessException, UnauthorizedException, InUseException {
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.WHITE, playerName, gameID);

        assertNull(gameDAO.getGame(gameID).blackUsername());
    }

    @Test
    public void updateBothPlayers() throws DataAccessException, UnauthorizedException, InUseException {
        gameDAO.clear();

        String gameName = "TestGame";
        String playerNameBlack = "Bob";
        String playerNameWhite = "Sherry";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.WHITE, playerNameWhite, gameID);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.BLACK, playerNameBlack, gameID);

        assertEquals(playerNameWhite, gameDAO.getGame(gameID).whiteUsername());
        assertEquals(playerNameBlack, gameDAO.getGame(gameID).blackUsername());
    }
}
