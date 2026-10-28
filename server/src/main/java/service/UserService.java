package service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.InUseException;
import dataaccess.memory.MemoryUserDataAccess;
import dataaccess.mysql.MySqlUserDataAccess;
import model.UserData;
import result.LoginResult;

import static service.AuthentificationService.generateAuthToken;

public class UserService {
    MySqlUserDataAccess userDAO = new MySqlUserDataAccess();

    public UserService() throws DataAccessException {}

    public void clear() throws DataAccessException {
        userDAO.clear();
    }

    public LoginResult register(UserData userData) throws InUseException, DataAccessException {
        String authToken = generateAuthToken();
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new DataAccessException("Missing registration input fields");
        }
        userDAO.createUser(userData.username(), userData.password(), userData.email());
        return new LoginResult(userData.username(), authToken);
    }

    public LoginResult login(UserData localUserData) throws DataAccessException, UnauthorizedException {
        String authToken = generateAuthToken();
        UserData serverUserData = userDAO.getUser(localUserData.username());
        if (localUserData.username() == null || localUserData.password() == null) {
            throw new DataAccessException("Missing username field!");
        }
        if (serverUserData == null || !localUserData.password().equals(serverUserData.password())) {
            throw new UnauthorizedException(String.format("Password %s is incorrect!", localUserData.password()));
        }
        return new LoginResult(localUserData.username(), authToken);
    }
}
