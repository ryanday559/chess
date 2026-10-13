package service;

import request.RegisterRequest;
import result.LoginResult;

public class UserService {

    public UserService() {}

    public LoginResult register(RegisterRequest request) {
        return request.getData();
    }
}
