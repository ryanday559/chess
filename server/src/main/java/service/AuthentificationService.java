package service;

import dataaccess.DataAccessException;
import dataaccess.TokenInUseException;
import dataaccess.UnauthorizedException;
import dataaccess.sql.MySqlAuthentificationDataAccess;
import model.AuthData;

import java.util.UUID;

public class AuthentificationService {
    private MySqlAuthentificationDataAccess authDAO = new MySqlAuthentificationDataAccess();

    public AuthentificationService() throws DataAccessException {
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void clear() throws DataAccessException {
        authDAO.clear();
    }

    public void updateAuth(String authToken, String username) throws TokenInUseException, DataAccessException {
        authDAO.createAuth(authToken, username);
    }

    public void authenticate(String authToken) throws UnauthorizedException, DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException(String.format("Invalid Authentification %s", authToken));
        }
    }

    public void removeAuth(String authToken) throws UnauthorizedException, DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken);
    }
}
