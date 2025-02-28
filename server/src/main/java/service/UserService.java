package service;


import dataaccess.DataAccessException;
import dataaccess.userdao.MemoryUserDao;
import dataaccess.userdao.UserDao;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class UserService extends Service{
    final private UserDao dataAccess = new MemoryUserDao();

    public RegisterResult register(RegisterRequest request) {

        if (request.username() == null || request.password() == null || request.email() == null ||
                request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            return new RegisterResult("Error: bad request");
        }

        UserData newUser = new UserData(request);
        try {
            if (dataAccess.getUser(request.username()) == null) {
                dataAccess.createUser(newUser);
            } else {
                return new RegisterResult("Error: already taken");
            }
        } catch (DataAccessException e) {
            return new RegisterResult("Error: Data Access Exception");
        }

        return new RegisterResult(login(new LoginRequest(request)));
    }

    public LoginResult login(LoginRequest request) {
        UserData user;
        try {
            user = dataAccess.getUser(request.username());
        } catch (DataAccessException e) {
            user = new UserData();
        }
        AuthData authData = new AuthData();
        if (user == null) {
            return new LoginResult("Error: unauthorized");
        } else if (request.password().equals(user.password())) {
            try {
                authData = authDataAccess.createAuth(user.username());
            } catch (DataAccessException e) {
                return new LoginResult("Error: Data Access Exception");
            }
        } else {
            return new LoginResult("Error: unauthorized");
        }

        return new LoginResult(request.username(), authData.authToken());
    }

    public LogoutResult logout(LogoutRequest request) {
        // first verify the auth data
        AuthData authData = null;
        try {
            authData = authorize(request.authToken());
        } catch (DataAccessException e) {
            return new LogoutResult("Error: Data Access Exception");
        }
        if (authData != null) {
            try {
                authDataAccess.removeAuth(request.authToken());
            } catch (DataAccessException e) {
                return new LogoutResult("Error: Data Access Exception");
            }
        } else {
            return new LogoutResult("Error: unauthorized");
        }
        return new LogoutResult("");
    }
}
