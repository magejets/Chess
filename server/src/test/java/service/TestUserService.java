package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class TestUserService {
    @Test
    public void TestRegisterPositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService service = new UserService();
        RegisterRequest request = new RegisterRequest("username", "password", "email@email.com");

        // run the function
        RegisterResult result = service.register(request);

        // test
        Assertions.assertEquals(request.username(), result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(36, result.authToken().length()); // length used because it is random and unpredictable
    }

    @Test
    public void TestRegisterNegative() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService service = new UserService();
        RegisterRequest request = new RegisterRequest("username", "password", "email@email.com");
        RegisterRequest invalidRequest = new RegisterRequest("username", "password", "email@email.com");
        service.register(request);

        // run the function
        RegisterResult result = service.register(invalidRequest);

        // test
        Assertions.assertEquals("Error: already taken", result.message());
    }

    @Test
    public void TestLoginPositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService service = new UserService();
        RegisterResult registerResult = service.register(new RegisterRequest("username", "password", "email@email.com"));
        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        service.logout(logoutRequest);
        LoginRequest request = new LoginRequest("username", "password");

        // run the function
        LoginResult result = service.login(request);

        // test
        Assertions.assertEquals(request.username(), result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(36, result.authToken().length()); // length because it is a random token
    }

    @Test
    public void TestLoginNegative() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService service = new UserService();
        RegisterResult registerResult = service.register(new RegisterRequest("username", "password", "email@email.com"));
        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        service.logout(logoutRequest);
        LoginRequest invalidRequest = new LoginRequest("username", "wrong-password");

        // run the function
        LoginResult result = service.login(invalidRequest);

        // test
        Assertions.assertEquals("Error: unauthorized", result.message());
    }

    @Test
    public void TestLogoutPositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService service = new UserService();
        RegisterResult registerResult = service.register(new RegisterRequest("username", "password", "email@email.com"));
        LogoutRequest request = new LogoutRequest(registerResult.authToken());
        AuthDao userDao = new MemoryAuthDao();

        // run the function
        LogoutResult result = service.logout(request);

        // test
        Assertions.assertEquals("", result.message());
        try {
            Assertions.assertNull(userDao.getAuth(registerResult.authToken()));
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void TestLogoutNegative() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService service = new UserService();
        service.register(new RegisterRequest("username", "password", "email@email.com"));
        LogoutRequest invalidRequest = new LogoutRequest("random-hash-auth-token-that-isn't-a-real-auth-token");

        // run the function
        LogoutResult result = service.logout(invalidRequest);

        // test
        Assertions.assertEquals("Error: unauthorized", result.message());
    }
}
