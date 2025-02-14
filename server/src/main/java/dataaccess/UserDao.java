package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserDao {
    // dummy map to stand in for database
    private HashMap<String, UserData> database = new HashMap<>();

    public UserData find(String username) {
        return database.get(username);
    }

    public void add(UserData newUser) {
        database.put(newUser.username(), newUser);
    }
}
