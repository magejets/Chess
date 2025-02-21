package dataaccess;

import dataaccess.userdao.MemoryUserDao;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserDaoTests {
    @Test
    public void testCreateAndGetUser() {
        MemoryUserDao testDao = new MemoryUserDao();
        UserData user = new UserData("mage", "12345", "mage@cs240.com");
        try {
            testDao.createUser(user);
        } catch (DataAccessException e) {

        }
        try {
            UserData gotUser = testDao.getUser("mage");
            Assertions.assertEquals(gotUser, user);
        } catch (DataAccessException e) {

        }
    }
}
