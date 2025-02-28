package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {
    private static final HashMap<String, UserData> database = new HashMap<>();


    public UserData getUser(String username) throws DataAccessException {
        return database.get(username);
    }

    public void createUser(UserData newUser) throws DataAccessException {
        database.put(newUser.username(), newUser);
    }

    public void clear() throws DataAccessException {
        database.clear();
    }
}
