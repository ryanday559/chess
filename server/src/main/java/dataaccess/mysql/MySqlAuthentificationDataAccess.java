package dataaccess.mysql;

import dataaccess.*;
import model.AuthData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.*;

public class MySqlAuthentificationDataAccess implements AuthentificationDataAccess {
    public MySqlAuthentificationDataAccess() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public AuthData createAuth(String authToken, String username) throws TokenInUseException, DataAccessException {
        var databaseValues = getFromDatabase("SELECT authToken FROM authentifications WHERE authToken=?", authToken);
        if (!databaseValues.isEmpty()) {
            throw new TokenInUseException("Token already in use!");
        }
        DatabaseManager.addToDatabase("INSERT INTO authentifications (authToken, username) VALUES(?, ?)", authToken, username);
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var databaseValues = getFromDatabase("SELECT authToken, username FROM authentifications WHERE authToken=?", authToken);
        if (databaseValues.isEmpty()) {
            return null;
        }
        var authValues = databaseValues.getFirst();
        return new AuthData((String) authValues.get(0), (String) authValues.get(1));
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException, DataAccessException {
        var databaseValues = getFromDatabase("SELECT authToken FROM authentifications WHERE authToken=?", authToken);
        if (databaseValues.isEmpty()) {
            throw new UnauthorizedException("User not authorized!");
        }
        deleteFromDatabase("DELETE FROM authentifications WHERE authToken=?", authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        deleteFromDatabase("TRUNCATE TABLE authentifications");
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
