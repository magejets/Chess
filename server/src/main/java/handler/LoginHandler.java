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
        LoginResult result = service.login(request);
        switch (result.message()) {
            case "Error: unauthorized":
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            case "Error: Data Access Exception":
                res.status(500);
                return "{ \"message\": \"Error: Data Access Exception\" }";
            case null, default:
                res.status(200);
                return new Gson().toJson(result, LoginResult.class);
        }
    }
}
