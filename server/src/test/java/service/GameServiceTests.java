package service;

import chess.ChessGame;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.InUseException;
import model.AuthData;
import model.GameData;
import model.GameJoinData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;





public class GameServiceTests {
    private GameService gameService = new GameService();

    public GameServiceTests() throws DataAccessException {
    }

    @BeforeEach
    public void clear() throws DataAccessException {
        gameService.clear();
    }


    @Test
    public void testClear() throws DataAccessException {
        GameData gameData = new GameData(1, null, null, "name", new ChessGame());
        gameService.createGame(gameData);
        gameService.clear();
        Collection<GameData> games = gameService.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void testListGames() throws DataAccessException {
        GameData gameData1 = new GameData(1, null, null, "name", new ChessGame());
        gameService.createGame(gameData1);
        GameData gameData2 = new GameData(2, null, null, "name", new ChessGame());
        gameService.createGame(gameData1);
        Collection<GameData> games = gameService.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void testCreateGameGivesValidGameID() throws DataAccessException {
        GameData gameData = new GameData(1, null, null, "name", new ChessGame());
        int id = gameService.createGame(gameData);
        assertTrue(id > 0);
    }

    @Test
    public void testJoinGameBlack() throws DataAccessException, InUseException {
        GameData gameData = new GameData(1, null, null, "name", new ChessGame());
        int id = gameService.createGame(gameData);
        String username = "user";
        AuthData authData = new AuthData("abc", username);
        GameJoinData gameJoinData = new GameJoinData(ChessGame.TeamColor.BLACK, id);
        gameService.joinGame(gameJoinData, authData);
        GameData game = gameService.listGames().iterator().next();
        assertEquals(username, game.blackUsername());
    }

    @Test
    public void testJoinGameWhite() throws DataAccessException, InUseException {
        GameData gameData = new GameData(1, null, null, "name", new ChessGame());
        int id = gameService.createGame(gameData);
        String username = "user";
        AuthData authData = new AuthData("abc", username);
        GameJoinData gameJoinData = new GameJoinData(ChessGame.TeamColor.WHITE, id);
        gameService.joinGame(gameJoinData, authData);
        GameData game = gameService.listGames().iterator().next();
        assertEquals(username, game.whiteUsername());
    }

    @Test
    public void testCannotCreateGameWithoutName() throws DataAccessException {
        GameData gameData = new GameData(1, null, null, null, new ChessGame());
        assertThrows(BadRequestException.class, ()->gameService.createGame(gameData));
    }

    @Test
    public void cannotJoinOccupiedGame() throws DataAccessException, InUseException {
        GameData gameData = new GameData(1, null, null, "name", new ChessGame());
        int id = gameService.createGame(gameData);
        String username = "user";
        AuthData authData = new AuthData("abc", username);
        GameJoinData whiteGameJoinData = new GameJoinData(ChessGame.TeamColor.WHITE, id);
        GameJoinData blackGameJoinData = new GameJoinData(ChessGame.TeamColor.BLACK, id);
        gameService.joinGame(whiteGameJoinData, authData);
        gameService.joinGame(blackGameJoinData, authData);
        assertThrows(InUseException.class, ()->gameService.joinGame(whiteGameJoinData, authData));
        assertThrows(InUseException.class, ()->gameService.joinGame(blackGameJoinData, authData));
    }

    @Test
    public void cannotJoinNonexistentGame() throws DataAccessException, InUseException {
        AuthData authData = new AuthData("abc", "bob");
        GameJoinData whiteGameJoinData = new GameJoinData(ChessGame.TeamColor.WHITE, 1);
        assertThrows(BadRequestException.class, ()->gameService.joinGame(whiteGameJoinData, authData));
    }
}
