package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao{
    private static HashMap<String, AuthData> database = new HashMap<>();

    public AuthData getAuth(String authToken) throws DataAccessException {
        return database.get(authToken);
    }

    public void removeAuth(String authToken) throws DataAccessException {
        database.remove(authToken);
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        database.put(authData.authToken(), authData);
        return authData;
    }

    public void clear() throws DataAccessException {
        database.clear();
    }
}
