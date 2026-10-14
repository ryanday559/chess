package dataaccess;

import model.AuthData;

public interface AuthentificationDataAccess {
    public AuthData createAuth(String authToken, String username) throws TokenInUseException;
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken) throws UnauthorizedException;
    public void clear();
}
