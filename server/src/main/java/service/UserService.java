package service;

import dataaccess.UsernameInUseException;
import dataaccess.memory.MemoryUserDataAccess;
import model.UserData;
import result.LoginResult;

import static service.AuthentificationService.generateAuthToken;

public class UserService {
    MemoryUserDataAccess userDAO = new MemoryUserDataAccess();

    public UserService() {}

    public void clear() {
        userDAO.clear();
    }

    public LoginResult register(UserData userData) throws UsernameInUseException {
        String authToken = generateAuthToken();
        userDAO.createUser(userData.username(), userData.password(), userData.email());
        return new LoginResult(userData.username(), authToken);
    }
}
