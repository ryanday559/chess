package dataaccess.memory;

import dataaccess.UserDataAccess;
import dataaccess.InUseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDataAccess implements UserDataAccess {
    private Map<String, UserData> userMap = new HashMap<String, UserData>();

    public MemoryUserDataAccess() {
    }

    public UserData getUser(String username) {
        return userMap.get(username);
    }

    public void createUser(String username, String password, String email) throws InUseException {
        if (userMap.containsKey(username)) {
            throw new InUseException("Username Already In Use!");
        }
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userMap.put(username, new UserData(username, encryptedPassword, email));
    }

    public void clear() {
        userMap = new HashMap<String, UserData>();
    }
}
