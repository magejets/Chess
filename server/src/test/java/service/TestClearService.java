package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.MemoryGameDao;
import dataaccess.userdao.MemoryUserDao;
import dataaccess.userdao.UserDao;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import result.ClearResult;

public class TestClearService {
    @Test
    public void TestClearUsersPositive() {
        UserDao userDao = new MemoryUserDao();
        ClearService service = new ClearService();
        try {
            userDao.createUser(new UserData("username", "password", "email@email.com"));
            userDao.createUser(new UserData("username1", "password", "email1@email.com"));
            ClearResult result = service.clear();
            Assertions.assertEquals("", result.message());
            Assertions.assertNull(userDao.getUser("username"));
            Assertions.assertNull(userDao.getUser("username1"));
        } catch (DataAccessException e) {

        }

    }

    @Test
    public void TestClearGamesPositive() {
        GameDao gameDao = new MemoryGameDao();
        ClearService service = new ClearService();
        try {
            gameDao.createGame(new GameData(0, "", "", "myGame", new ChessGame()));
            gameDao.createGame(new GameData(1, "", "", "myOtherGame", new ChessGame()));
            ClearResult result = service.clear();
            Assertions.assertEquals("", result.message());
            Assertions.assertEquals(0, gameDao.getGames().size());
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void TestClearAuthPositive() {
        AuthDao authDao = new MemoryAuthDao();
        ClearService service = new ClearService();
        try {
            AuthData token1 = authDao.createAuth("user1");
            AuthData token2 = authDao.createAuth("user2");
            ClearResult result = service.clear();
            Assertions.assertEquals("", result.message());
            Assertions.assertNull(authDao.getAuth(token1.authToken()));
            Assertions.assertNull(authDao.getAuth(token2.authToken()));
        } catch (DataAccessException e) {

        }
    }
}
