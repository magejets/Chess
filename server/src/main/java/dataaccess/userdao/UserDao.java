package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDao {
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(UserData newUser) throws DataAccessException;
    public void clear() throws DataAccessException;
}
