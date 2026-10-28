package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.InUseException;
import dataaccess.UserDataAccess;
import model.UserData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.*;

public class MySqlUserDataAccess implements UserDataAccess {
    public MySqlUserDataAccess() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var databaseValues = getFromDatabase("SELECT username, password, email FROM users WHERE username=?", username);
        if (databaseValues.isEmpty()) {
            return null;
        }
        var result = databaseValues.getFirst();
        return new UserData((String) result.get(0), (String) result.get(1), (String) result.get(2));
    }

    @Override
    public void createUser(String username, String password, String email) throws InUseException, DataAccessException {
        var databaseValues = getFromDatabase("SELECT username FROM users WHERE username=?", username);
        if (!databaseValues.isEmpty()) {
            throw new InUseException("Username already in use!");
        }
        addToDatabase("INSERT INTO users (username, password, email) VALUES(?, ?, ?)", username, password, email);
    }

    @Override
    public void clear() throws DataAccessException {
        deleteFromDatabase("TRUNCATE TABLE users");
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users(
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
