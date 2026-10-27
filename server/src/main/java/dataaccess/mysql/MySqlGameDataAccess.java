package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import dataaccess.InUseException;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.configureDatabase;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public Collection<GameData> getGames() {
        return List.of();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return DatabaseManager.addToDatabase("INSERT INTO games (gameName) VALUES(?)", gameName);
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updatePlayerColor(ChessGame.TeamColor playerColor, String username, int gameID) throws DataAccessException, InUseException {
        if (playerColor == ChessGame.TeamColor.BLACK) {
            DatabaseManager.addToDatabase("UPDATE games SET blackPlayer=? WHERE id=?", username, gameID);
        }
        else {
            DatabaseManager.addToDatabase("UPDATE games SET whitePlayer=? WHERE id=?", username, gameID);
        }
    }

    @Override
    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games(
                `id` int(256) NOT NULL AUTO_INCREMENT,
                `gameName` varchar(256) NOT NULL,
                `blackPlayer` varchar(256),
                `whitePlayer` varchar(256),
                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
