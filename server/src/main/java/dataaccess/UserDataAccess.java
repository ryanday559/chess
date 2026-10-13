package dataaccess;

import model.UserData;

public interface UserDataAccess {
    public UserData getUser(String username);
    public void createUser(UserData userData);
    public void clear();
}
