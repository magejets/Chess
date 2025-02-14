package handler;

import com.google.gson.Gson;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import service.UserService;
import spark.Request;

public class LoginHandler {
    Request req;
    public LoginHandler(Request req) { // this is the same as the register handler. Use inheritance to reduce redundancy
        this.req = req;
    }

    public String login() {
        var request = new Gson().fromJson(req.body(), LoginRequest.class);
        var service = new UserService();
        return new Gson().toJson(service.login(request), LoginResult.class);
    }
}
