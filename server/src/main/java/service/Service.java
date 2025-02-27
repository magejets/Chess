package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import model.AuthData;

import java.util.Map;

public class Service {
    final AuthDao authDataAccess = new MemoryAuthDao();
    public AuthData authorize(String authToken) throws DataAccessException {
        try {
            return authDataAccess.getAuth(authToken);
        } catch (DataAccessException e) {
            throw e;
        }
    }
}
