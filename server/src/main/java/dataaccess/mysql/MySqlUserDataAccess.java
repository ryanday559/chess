package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.InUseException;
import dataaccess.UserDataAccess;
import model.UserData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.configureDatabase;

public class MySqlUserDataAccess implements UserDataAccess {
    public MySqlUserDataAccess() throws DataAccessException {
        configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws InUseException {

    }

    @Override
    public void clear() {

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
