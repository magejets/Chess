package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {
    // dummy map to stand in for database
    private static HashMap<String, UserData> database = new HashMap<>();


    public UserData getUser(String username) throws DataAccessException {
        return database.get(username);
    }

    public void createUser(UserData newUser) throws DataAccessException {
        database.put(newUser.username(), newUser);
    }
}
