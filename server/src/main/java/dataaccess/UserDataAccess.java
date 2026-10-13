package dataaccess;

import model.UserData;

public interface UserDataAccess {
    public UserData getUser(String username);
    public void createUser(String username, String password, String email) throws UsernameInUseException;
    public void clear();
}
