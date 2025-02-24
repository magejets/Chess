package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao{
    // dummy map to stand in for database
    private static HashMap<String, AuthData> database = new HashMap<>();

    public AuthData getAuth(String authToken) throws DataAccessException {

        return database.get(authToken);
    }

//    public void add(AuthData newAuth) {
//        database.put(newAuth.authToken(), newAuth);
//    }

    public void removeAuth(String authToken) throws DataAccessException {
        database.remove(authToken);
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        database.put(authData.authToken(), authData);
        return authData;
    }
}
