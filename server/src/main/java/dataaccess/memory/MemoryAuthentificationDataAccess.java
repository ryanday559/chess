package dataaccess.memory;

import dataaccess.AuthentificationDataAccess;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthentificationDataAccess implements AuthentificationDataAccess {
    private Map<Integer, AuthData> authentificationMap = new HashMap<Integer, AuthData>();

    public MemoryAuthentificationDataAccess() {
    }

    public AuthData createAuth(int authToken) {
        AuthData authData = new AuthData(authToken);
        authentificationMap.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(int authToken) {
        return authentificationMap.get(authToken);
    }

    public void deleteAuth(int authToken) {

    }

    public void clear() {

    }
}
