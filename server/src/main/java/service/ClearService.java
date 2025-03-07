package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import dataaccess.authdao.SQLAuthDao;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.MemoryGameDao;
import dataaccess.gamedao.SQLGameDao;
import dataaccess.userdao.MemoryUserDao;
import dataaccess.userdao.SQLUserDao;
import dataaccess.userdao.UserDao;
import result.ClearResult;

public class ClearService extends Service{
    private GameDao gameDataAccess = new MemoryGameDao();
    private UserDao userDataAccess = new MemoryUserDao();

    public ClearService(UserDao userDao, AuthDao authDao, GameDao gameDao) {
        super(authDao);
        this.userDataAccess = userDao;
        this.gameDataAccess = gameDao;
    }

    // the following constructor is for the tests only and when NOT working with http
    public ClearService() {
        super(new SQLAuthDao());
        this.userDataAccess = new SQLUserDao();
        this.gameDataAccess = new SQLGameDao();
    }

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
