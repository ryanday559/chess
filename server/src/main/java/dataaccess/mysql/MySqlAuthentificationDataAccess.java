package dataaccess.mysql;

import dataaccess.*;
import model.AuthData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.configureDatabase;

public class MySqlAuthentificationDataAccess implements AuthentificationDataAccess {
    public MySqlAuthentificationDataAccess() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public AuthData createAuth(String authToken, String username) throws TokenInUseException, DataAccessException {
        DatabaseManager.addToDatabase("INSERT INTO authentifications (authToken, username) VALUES(?)", authToken, username);
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {

    }

    @Override
    public void clear() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authentifications(
                `authToken` varchar(256) NOT NULL,
                `username` varchar(256) NOT NULL,
                PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
