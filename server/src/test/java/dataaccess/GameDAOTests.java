package dataaccess;

import chess.ChessGame;
import dataaccess.memory.MemoryGameDataAccess;
import dataaccess.mysql.MySqlGameDataAccess;
import model.GameData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {
    public GameDAOTests() throws DataAccessException {
    }

    private GameDataAccess getDataAccessType(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException {
        return switch (dataAccessType) {
            case MEMORY -> new MemoryGameDataAccess();
            case MYSQL -> new MySqlGameDataAccess();
        };
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void clearTest(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
        gameDAO.clear();

        String gameName = "TestGame";
        int gameID = gameDAO.createGame(gameName);

        gameDAO.clear();

        assertNull(gameDAO.getGame(gameID));
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void createGetGameTest(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
        gameDAO.clear();

        String gameName = "TestGame";
        int gameID = gameDAO.createGame(gameName);
        GameData gameData = gameDAO.getGame(gameID);

        assertEquals(gameID, gameData.gameID());
        assertEquals(gameName, gameData.gameName());
        assertEquals(ChessGame.class, gameData.game().getClass());
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void getGamesReturnsAllGames(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
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

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void updateBlackPlayer(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException, UnauthorizedException, InUseException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.BLACK, playerName, gameID);

        assertEquals(playerName, gameDAO.getGame(gameID).blackUsername());
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void updateBlackPlayerDoesNotAffectWhitePlayer(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException, UnauthorizedException, InUseException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.BLACK, playerName, gameID);

        assertNull(gameDAO.getGame(gameID).whiteUsername());
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void updateWhitePlayer(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException, UnauthorizedException, InUseException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.WHITE, playerName, gameID);

        assertEquals(playerName, gameDAO.getGame(gameID).whiteUsername());
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void updateWhitePlayerDoesNotAffectBlackPlayer(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException, UnauthorizedException, InUseException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
        gameDAO.clear();

        String gameName = "TestGame";
        String playerName = "Bob";
        int gameID = gameDAO.createGame(gameName);
        gameDAO.updatePlayerColor(ChessGame.TeamColor.WHITE, playerName, gameID);

        assertNull(gameDAO.getGame(gameID).blackUsername());
    }

    @ParameterizedTest
    @EnumSource(AuthentificationDAOTests.authType.class)
    public void updateBothPlayers(AuthentificationDAOTests.authType dataAccessType) throws DataAccessException, UnauthorizedException, InUseException {
        GameDataAccess gameDAO = getDataAccessType(AuthentificationDAOTests.authType.MEMORY);
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
