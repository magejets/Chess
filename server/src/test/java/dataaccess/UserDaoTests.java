package dataaccess;

import dataaccess.userdao.*;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserDaoTests {
    @Test
    public void testCreateAndGetUser() {
        UserDao testDao = new SQLUserDao();
        UserData user = new UserData("mage", "12345", "mage@cs240.com");
        try {
            testDao.clear();
            testDao.createUser(user);
            UserData gotUser = testDao.getUser("mage");
            Assertions.assertEquals(gotUser, user);
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testPersistenceUser() {
        UserDao dao1 = new SQLUserDao();
        UserData user = new UserData("mage", "12345", "mage@cs240.com");
        try {
            dao1.clear();
            dao1.createUser(user);
            UserDao dao2 = new SQLUserDao();
            UserData gotUser = dao2.getUser("mage");
            Assertions.assertEquals(gotUser, user);
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testClear() {
        UserDao testDao = new SQLUserDao();
        UserData user = new UserData("mage", "12345", "mage@cs240.com");
        try {
            testDao.createUser(user);
            testDao.clear();
            UserData gotUser = testDao.getUser("mage");
            Assertions.assertNull(gotUser);
        } catch (DataAccessException e) {

        }
    }
}
