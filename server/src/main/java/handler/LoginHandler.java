package handler;

import com.google.gson.Gson;
import request.LoginRequest;
import result.LoginResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.body(), LoginRequest.class);
        var service = new UserService();
        res.status(200);
        return new Gson().toJson(service.login(request), LoginResult.class);
    }
}
