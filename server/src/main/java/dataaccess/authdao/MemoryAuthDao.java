package dataaccess.authdao;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao{
    // dummy map to stand in for database
    private static HashMap<String, AuthData> database = new HashMap<>();

    public AuthData getAuth(String authToken) {

        return database.get(authToken);
    }

//    public void add(AuthData newAuth) {
//        database.put(newAuth.authToken(), newAuth);
//    }

    public void removeAuth(String authToken) {
        database.remove(authToken);
    }

    public AuthData createAuth(String username) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        database.put(authData.username(), authData);
        return authData;
    }
}
