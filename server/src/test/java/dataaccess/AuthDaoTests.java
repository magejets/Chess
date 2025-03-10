package dataaccess;

import dataaccess.authdao.AuthDao;
import dataaccess.authdao.SQLAuthDao;
import dataaccess.userdao.SQLUserDao;
import dataaccess.userdao.UserDao;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDaoTests {
    @BeforeAll
    public static void init() {
        UserDao initializer = new SQLUserDao(); // just to initialize the databases
    }

    AuthDao testDao = new SQLAuthDao();
    AuthData authData;
    @BeforeEach
    public void testInit() {
        try {
            testDao.clear();
            authData = testDao.createAuth("mage");
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testCreateAuth() {
        Assertions.assertEquals("mage", authData.username());
        Assertions.assertEquals(36, authData.authToken().length());
    }

    @Test
    public void testCreateAndGetAuth() {
        try {
            AuthData gotData = testDao.getAuth(authData.authToken());
            Assertions.assertEquals(gotData.username(), authData.username());
            Assertions.assertEquals(authData.authToken(), gotData.authToken());
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testRemoveAuth() {
        try {
            testDao.removeAuth(authData.authToken());
            authData = testDao.getAuth(authData.authToken());
        } catch (DataAccessException e) {

        }

        Assertions.assertNull(authData);
    }

    @Test
    public void testClear() {
        try {
            testDao.clear();
            AuthData gotAuth = testDao.getAuth(authData.authToken());
            Assertions.assertNull(gotAuth);
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testCreateNegative() {
        try {
            testDao.createAuth(null);
        } catch (DataAccessException e) {
            Assertions.assertEquals(e.getMessage(), "Column 'username' cannot be null");
        }
    }

    @Test
    public void testGetNegative() {
        try {
            AuthData authDataBad = testDao.getAuth("invalid-auth-token");
            Assertions.assertNull(authDataBad);
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testRemoveNegative() {
        try {
            int rowsRemoved = testDao.removeAuth("invalid-auth-token");
            Assertions.assertEquals(0, rowsRemoved);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }
}

