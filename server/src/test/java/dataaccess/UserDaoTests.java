package dataaccess;

import dataaccess.userdao.MemoryUserDao;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserDaoTests {
    @Test
    public void testCreateAndGetUser() {
        MemoryUserDao testDao = new MemoryUserDao();
        UserData user = new UserData("mage", "12345", "mage@cs240.com");
        testDao.createUser(user);
        Assertions.assertEquals(testDao.getUser("mage"), user);
    }
}
