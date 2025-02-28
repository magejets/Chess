package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import model.AuthData;

public class Service {
    final AuthDao authDataAccess = new MemoryAuthDao();
    public AuthData authorize(String authToken) throws DataAccessException {
        return authDataAccess.getAuth(authToken);
    }
}
