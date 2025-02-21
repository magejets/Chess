package service;


import dataaccess.userdao.MemoryUserDao;
import dataaccess.userdao.UserDao;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class UserService {
    final private UserDao dataAccess = new MemoryUserDao();
    final private AuthDao authDataAccess = new MemoryAuthDao();

    public RegisterResult register(RegisterRequest request) {

        UserData newUser = new UserData(request);
        if (dataAccess.getUser(request.username()) == null) {
            dataAccess.createUser(newUser);
        } else {
            // error handling, 403 already exists
        }

        return new RegisterResult(login(new LoginRequest(request)));
    }

    public LoginResult login(LoginRequest request) {
        UserData user = dataAccess.getUser(request.username());
        AuthData authData;
        if (user == null) {
            // error 401?
            return null; // probably will want to find a way I can return an error message
        } else if (request.password().equals(user.password())) {
            authData = authDataAccess.createAuth(user.username());
        } else {
            // error 401, unauthorized
            return null; // same as above
        }

        return new LoginResult(request.username(), authData.authToken());
    }

    public LogoutResult logout(LogoutRequest request) {
        // first verify the auth data
        AuthData authData = authDataAccess.getAuth(request.authToken());
        if (authData != null) {
            // remove the auth data
            authDataAccess.removeAuth(request.authToken());
        } else {
            // error of some sort
        }
        return new LogoutResult();
    }
}
