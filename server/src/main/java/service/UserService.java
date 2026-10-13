package service;

import dataaccess.DataAccessException;
import dataaccess.UsernameInUseException;
import dataaccess.memory.MemoryUserDataAccess;
import model.UserData;
import result.LoginResult;

import javax.xml.crypto.Data;

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

    public LoginResult login(UserData localUserData) throws DataAccessException {
        String authToken = generateAuthToken();
        UserData serverUserData = userDAO.getUser(localUserData.username());
        if (!localUserData.password().equals(serverUserData.password())) {
            throw new DataAccessException(String.format("Password %s is incorrect!", localUserData.password()));
        }
        return new LoginResult(localUserData.username(), authToken);
    }
}
