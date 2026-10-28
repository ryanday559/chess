package dataaccess;

import model.AuthData;

public interface AuthentificationDataAccess {
    public AuthData createAuth(String authToken, String username) throws TokenInUseException, DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws UnauthorizedException, DataAccessException;
    public void clear();
}
