package dataaccess.memory;

import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.UsernameInUseException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDataAccess implements UserDataAccess {
    private Map<String, UserData> userMap = new HashMap<String, UserData>();

    public MemoryUserDataAccess() {
    }

    public UserData getUser(String username) {
        return userMap.get(username);
    }

    public void createUser(String username, String password, String email) throws UsernameInUseException {
        if (userMap.containsKey(username)) {
            throw new UsernameInUseException("Username Already In Use!");
        }
        userMap.put(username, new UserData(username, password, email));
    }

    public void clear() {
        userMap = new HashMap<String, UserData>();
    }
}
