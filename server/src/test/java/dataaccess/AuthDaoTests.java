package dataaccess;

import dataaccess.authdao.MemoryAuthDao;
import dataaccess.userdao.MemoryUserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthDaoTests {

    @Test
    public void testCreateAndGetUser() {
        MemoryAuthDao testDao = new MemoryAuthDao();
        AuthData authData = new AuthData("random-hash-id","mage");
        try {
            testDao.createAuth(authData.username());
        } catch (DataAccessException e) {

        }
        try {
            AuthData gotData = testDao.getAuth("mage");
            Assertions.assertEquals(gotData.username(), authData.username());
            Assertions.assertEquals(36, gotData.authToken().length());
        } catch (DataAccessException e) {

        }
    }
}

