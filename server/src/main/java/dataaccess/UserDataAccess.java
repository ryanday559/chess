package dataaccess;

import model.UserData;

public interface UserDataAccess {
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(String username, String password, String email) throws InUseException, DataAccessException;
    public void clear() throws DataAccessException;
}
