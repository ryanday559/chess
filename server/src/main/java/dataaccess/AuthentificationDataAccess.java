package dataaccess;

import model.AuthData;

public interface AuthentificationDataAccess {
    public AuthData createAuth(String authToken, String username);
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken);
    public void clear();
}
