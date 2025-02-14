package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDao {
    // dummy map to stand in for database
    private static HashMap<String, AuthData> database = new HashMap<>();

    public AuthData find(String authToken) {
        return database.get(authToken);
    }

    public void add(AuthData newAuth) {
        database.put(newAuth.authToken(), newAuth);
    }

    public void remove(String authToken) {
        database.remove(authToken);
    }
}
