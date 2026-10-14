package dataaccess.memory;

import dataaccess.AuthentificationDataAccess;
import dataaccess.TokenInUseException;
import dataaccess.UnauthorizedException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthentificationDataAccess implements AuthentificationDataAccess {
    private Map<String, AuthData> authentificationMap = new HashMap<String, AuthData>();

    public MemoryAuthentificationDataAccess() {
    }

    public AuthData createAuth(String authToken, String username) throws TokenInUseException {
        if (authentificationMap.containsKey(authToken)) {
            throw new TokenInUseException("Authentification token already in use!");
        }
        AuthData authData = new AuthData(authToken, username);
        authentificationMap.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        return authentificationMap.get(authToken);
    }

    public void deleteAuth(String authToken) throws UnauthorizedException {
        if (!authentificationMap.containsKey(authToken)) {
            throw new UnauthorizedException(String.format("Authentication %s token not in use", authToken));
        }
        authentificationMap.remove(authToken);
    }

    public void clear() {
        authentificationMap = new HashMap<String, AuthData>();
    }
}
