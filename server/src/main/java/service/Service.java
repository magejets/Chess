package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import model.AuthData;

public class Service {
    protected AuthDao authDataAccess;

    public Service(AuthDao authDao) {
        this.authDataAccess = authDao;
    }

    public AuthData authorize(String authToken) throws DataAccessException {
        return authDataAccess.getAuth(authToken);
    }
}
