package service;

import dataaccess.DataAccessException;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.MemoryGameDao;
import dataaccess.userdao.MemoryUserDao;
import dataaccess.userdao.UserDao;
import result.ClearResult;

public class ClearService extends Service{
    final GameDao gameDataAccess = new MemoryGameDao();
    final UserDao userDataAccess = new MemoryUserDao();

    public ClearResult clear() {
        try {
            userDataAccess.clear();
            gameDataAccess.clear();
            authDataAccess.clear();
        } catch (DataAccessException e) {
            return new ClearResult("Error: Data Access Exception");
        }

        return new ClearResult("");
    }
}
