package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import dataaccess.InUseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySqlGameDataAccess implements GameDataAccess {
    public MySqlGameDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public Collection<GameData> getGames() {
        return List.of();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updatePlayerColor(ChessGame.TeamColor playerColor, String username, int gameID) throws DataAccessException, InUseException {

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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot Connect to Database!");
        }
    }
}
