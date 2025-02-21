package dataaccess.userdao;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDao implements UserDao {
    // dummy map to stand in for database
    private static HashMap<String, UserData> database = new HashMap<>();


    public UserData getUser(String username) {
        return database.get(username);
    }

    public void createUser(UserData newUser) {
        database.put(newUser.username(), newUser);
    }
}
