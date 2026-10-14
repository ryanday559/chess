package service;

import dataaccess.DataAccessException;
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

    public LoginResult register(UserData userData) throws UsernameInUseException, DataAccessException {
        String authToken = generateAuthToken();
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new DataAccessException("Missing registration input fields");
        }
        userDAO.createUser(userData.username(), userData.password(), userData.email());
        return new LoginResult(userData.username(), authToken);
    }

    public LoginResult login(UserData localUserData) throws DataAccessException {
        String authToken = generateAuthToken();
        UserData serverUserData = userDAO.getUser(localUserData.username());
        if (localUserData.username() == null || localUserData.password() == null) {
            throw new DataAccessException("Missing username field!");
        }
        if (!localUserData.password().equals(serverUserData.password())) {
            throw new DataAccessException(String.format("Password %s is incorrect!", localUserData.password()));
        }
        return new LoginResult(localUserData.username(), authToken);
    }
}
