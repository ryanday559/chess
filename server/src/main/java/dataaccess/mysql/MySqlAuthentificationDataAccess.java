package dataaccess.mysql;

import dataaccess.*;
import model.AuthData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.configureDatabase;
import static dataaccess.DatabaseManager.getFromDatabase;

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
    public AuthData getAuth(String authToken) throws DataAccessException {
        var databaseValues = getFromDatabase("SELECT authToken, username FROM authentifications WHERE authToken=?", authToken);
        var authValues = databaseValues.getFirst();
        new AuthData((String) authValues.get(0), (String) authValues.get(1));
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
