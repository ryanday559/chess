package dataaccess.mysql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import dataaccess.InUseException;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.*;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public Collection<GameData> getGames() throws DataAccessException {
        var games = getFromDatabase("SELECT id, whitePlayer, blackPlayer, gameName, chessGame FROM games");
        ArrayList<GameData> gamesData = new ArrayList<>();
        for (var game : games) {
            ChessGame chessGame = new Gson().fromJson((String) game.get(4), ChessGame.class);
            GameData gameData = new GameData(
                    (Integer) game.get(0),
                    (String) game.get(1),
                    (String) game.get(2),
                    (String) game.get(3),
                    chessGame);
            gamesData.add(gameData);
        }
        return gamesData;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String jsonGame = new Gson().toJson(game);
        return addToDatabase("INSERT INTO games (gameName, blackPlayer, WhitePlayer, chessGame) VALUES(?, ?, ?, ?)",
                gameName, null, null, jsonGame);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var databaseValues = getFromDatabase("SELECT id, whitePlayer, blackPlayer, gameName, chessGame FROM games WHERE id=?", gameID);
        if (databaseValues.isEmpty()) {
            return null;
        }
        var gameValues = databaseValues.getFirst();
        ChessGame chessGame = new Gson().fromJson((String) gameValues.get(4), ChessGame.class);
        GameData gameData = new GameData(
                (Integer) gameValues.get(0),
                (String) gameValues.get(1),
                (String) gameValues.get(2),
                (String) gameValues.get(3),
                chessGame);
        return gameData;
    }

    @Override
    public void updatePlayerColor(ChessGame.TeamColor playerColor, String username, int gameID) throws DataAccessException, InUseException {
        var games = getFromDatabase("SELECT id, blackPlayer, whitePlayer FROM games WHERE id=?", gameID);
        if (games.isEmpty()) {
            throw new DataAccessException("No valid games with given ID");
        }
        if (playerColor == ChessGame.TeamColor.BLACK) {
            if (games.getFirst().get(1) != null) {
                throw new InUseException("Player already claimed!");
            }
            addToDatabase("UPDATE games SET blackPlayer=? WHERE id=?", username, gameID);
        }
        else {
            if (games.getFirst().get(2) != null) {
                throw new InUseException("Player already claimed!");
            }
            addToDatabase("UPDATE games SET whitePlayer=? WHERE id=?", username, gameID);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        deleteFromDatabase("TRUNCATE TABLE games");
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games(
                `id` int NOT NULL AUTO_INCREMENT,
                `gameName` varchar(256) NOT NULL,
                `blackPlayer` varchar(256),
                `whitePlayer` varchar(256),
                `chessGame` text,
                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
