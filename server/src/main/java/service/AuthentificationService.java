package service;

import dataaccess.DataAccessException;
import dataaccess.TokenInUseException;
import dataaccess.memory.MemoryAuthentificationDataAccess;

import java.util.UUID;

public class AuthentificationService {
    private MemoryAuthentificationDataAccess authDAO = new MemoryAuthentificationDataAccess();

    public AuthentificationService() {
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void clear() {
        authDAO.clear();
    }

    public void updateAuth(String authToken, String username) throws TokenInUseException {
        authDAO.createAuth(authToken, username);
    }

    public void authenticate(String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new DataAccessException(String.format("Invalid Authentification %s", authToken));
        }
    }

    public void removeAuth(String authToken) {
        authDAO.deleteAuth(authToken);
    }
}
