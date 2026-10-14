package service;

import dataaccess.DataAccessException;
import dataaccess.TokenInUseException;
import dataaccess.UnauthorizedException;
import dataaccess.memory.MemoryAuthentificationDataAccess;
import model.AuthData;

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

    public void authenticate(String authToken) throws UnauthorizedException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException(String.format("Invalid Authentification %s", authToken));
        }
    }

    public void removeAuth(String authToken) throws UnauthorizedException {
        authDAO.deleteAuth(authToken);
    }

    public AuthData getAuthData(String authToken) {
        return authDAO.getAuth(authToken);
    }
}
