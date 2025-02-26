package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDao {
    public AuthData createAuth(String username) throws DataAccessException;
    public void removeAuth(String authToken) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
}
