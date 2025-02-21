package dataaccess.userdao;

import model.UserData;

public interface UserDao {
    public UserData getUser(String username);
    public void createUser(UserData newUser);
}
