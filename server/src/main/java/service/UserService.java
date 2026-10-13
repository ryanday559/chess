package service;

import dataaccess.memory.MemoryUserDataAccess;

public class UserService {
    MemoryUserDataAccess userDAO = new MemoryUserDataAccess();

    public UserService() {}

    public void clear() {
        userDAO.clear();
    }
}
