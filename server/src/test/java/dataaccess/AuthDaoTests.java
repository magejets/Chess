package dataaccess;

import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import dataaccess.authdao.SQLAuthDao;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDaoTests {
    @Test
    public void testCreateAuth() {
        AuthDao testDao = new SQLAuthDao();
        AuthData authData;
        try {
            testDao.clear();
            authData = testDao.createAuth("mage");
            AuthData gotData = testDao.getAuth(authData.authToken());
            Assertions.assertEquals("mage", authData.username());
            Assertions.assertEquals(36, authData.authToken().length());
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testCreateAndGetAuth() {
        AuthDao testDao = new SQLAuthDao();
        AuthData authData = new AuthData("random-hash-id","uninitialized-user");
        try {
            authData = testDao.createAuth("mage");
            AuthData gotData = testDao.getAuth(authData.authToken());
            Assertions.assertEquals(gotData.username(), authData.username());
            Assertions.assertEquals(authData.authToken(), gotData.authToken());
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testRemoveAuth() {
        AuthDao testDao = new MemoryAuthDao();
        AuthData authData = new AuthData("random-hash-id","mage");
        try {
            authData = testDao.createAuth(authData.username());
        } catch (DataAccessException e) {

        }
        try {
            testDao.removeAuth(authData.authToken());
            authData = testDao.getAuth(authData.authToken());
        } catch (DataAccessException e) {

        }

        Assertions.assertNull(authData);
    }
}

