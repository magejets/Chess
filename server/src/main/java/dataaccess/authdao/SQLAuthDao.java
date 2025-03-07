package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

public class SQLAuthDao implements AuthDao{
    public AuthData createAuth(String username) throws DataAccessException {
        return new AuthData();
        // INSERT INTO auth (authToken, username) VALUES (@authToken, @username)
    }
    public void removeAuth(String authToken) throws DataAccessException {
        // DELETE FROM auth WHERE authToken = @authToken
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return new AuthData();
        // SELECT authToken, username FROM auth WHERE authToken = @authToken
    }
    public void clear() throws DataAccessException {
        // DELETE FROM auth
    }
}
