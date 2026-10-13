package service;

import java.util.UUID;

public class AuthentificationService {

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
