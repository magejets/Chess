package service;

import dataaccess.AuthDao;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class UserService {
    final private UserDao dataAccess = new UserDao();
    final private AuthDao authDataAccess = new AuthDao();

    public RegisterResult register(RegisterRequest request) {

        UserData newUser = new UserData(request);
        if (getUser(request.username()) == null) {
            createUser(newUser);
        } else {
            // error handling, 403 already exists
        }

        return new RegisterResult(login(new LoginRequest(request)));

        //return result;
    }

    public LoginResult login(LoginRequest request) {
        UserData user = getUser(request.username());
        AuthData authData;
        if (user == null) {
            // error 401?
            return null; // probably will want to find a way I can return an error message
        } else if (request.password().equals(user.password())) {
            authData = createAuth(user.username());
        } else {
            // error 401, unauthorized
            return null; // same as above
        }

        return new LoginResult(request.username(), authData.authToken());
    }

    public LogoutResult logout(LogoutRequest request) {
        // first verify the auth data
        AuthData authData = authDataAccess.find(request.authToken());
        if (authData != null) {
            // remove the auth data
            authDataAccess.remove(request.authToken());
        } else {
            // error of some sort
        }
        return new LogoutResult();
    }

    private AuthData createAuth(String username) {
        AuthData authData = new AuthData("randomhash", username);
        authDataAccess.add(authData);
        return authData; // the auth token is a dummy! replace with random generated
    }

    private UserData getUser(String username) {
        return dataAccess.find(username);
    }

    private void createUser(UserData newUser) {
        dataAccess.add(newUser);
    }
}
