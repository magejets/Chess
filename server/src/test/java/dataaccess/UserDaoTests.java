package dataaccess;

import dataaccess.userdao.*;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDaoTests {
    UserDao testDao = new SQLUserDao();
    UserData user = new UserData("mage", "12345", "mage@cs240.com");

    @BeforeEach
    public void testInit() {
        try {
            testDao.clear();
            testDao.createUser(user);
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testCreateAndGetUser() {
        try {
            UserData gotUser = testDao.getUser("mage");
            Assertions.assertEquals(gotUser.username(), user.username());
            Assertions.assertEquals(gotUser.email(), user.email());
            Assertions.assertTrue(BCrypt.checkpw(user.password(), gotUser.password()));
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testPersistenceUser() {
        try {
            UserDao dao2 = new SQLUserDao();
            UserData gotUser = dao2.getUser("mage");
            Assertions.assertEquals(gotUser.username(), user.username());
            Assertions.assertEquals(gotUser.email(), user.email());
            Assertions.assertTrue(BCrypt.checkpw(user.password(), gotUser.password()));
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testClear() {
        try {
            testDao.clear();
            UserData gotUser = testDao.getUser("mage");
            Assertions.assertNull(gotUser);
        } catch (DataAccessException e) {
            Assertions.fail("Data Access Error");
        }
    }
}
