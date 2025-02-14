package service;

import dataaccess.AuthDao;
import dataaccess.UserDao;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class UserService {
    final private UserDao dataAccess = new UserDao();

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


    private AuthData createAuth(String username) {
        AuthData authData = new AuthData("randomhash", username);
        AuthDao authDataAccess = new AuthDao();
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
