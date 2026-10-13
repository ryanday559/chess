package service;

import dataaccess.memory.MemoryAuthentificationDataAccess;

import java.util.UUID;

public class AuthentificationService {
    private MemoryAuthentificationDataAccess authDAO = new MemoryAuthentificationDataAccess();

    public AuthentificationService() {
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void clear() {
        authDAO.clear();
    }
}
