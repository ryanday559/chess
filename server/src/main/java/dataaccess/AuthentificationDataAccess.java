package dataaccess;

import model.AuthData;

public interface AuthentificationDataAccess {
    public AuthData createAuth(int authToken);
    public AuthData getAuth(int authToken);
    public void deleteAuth(int authToken);
}
