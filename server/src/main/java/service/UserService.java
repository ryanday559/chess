package service;

import dataaccess.UsernameInUseException;
import dataaccess.memory.MemoryGameDataAccess;
import dataaccess.memory.MemoryUserDataAccess;
import model.UserData;
import request.RegisterRequest;
import result.LoginResult;

import static service.AuthentificationService.generateAuthToken;

public class UserService {
    MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();

    public UserService() {}

    public LoginResult register(RegisterRequest request) throws UsernameInUseException {
        UserData requestData = request.getData();
        userDataAccess.createUser(requestData.username(), requestData.password(), requestData.email());
        String authToken = generateAuthToken();
        return new LoginResult(requestData.username(), authToken);
    }


}
