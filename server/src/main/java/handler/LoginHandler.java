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
        return switch (result.message()) {
            case "Error: unauthorized" -> {
                res.status(401);
                yield "{ \"message\": \"Error: unauthorized\" }";
            }
            case "Error: Data Access Exception" -> {
                res.status(500);
                yield "{ \"message\": \"Error: Data Access Exception\" }";
            }
            case null, default -> {
                res.status(200);
                yield new Gson().toJson(result, LoginResult.class);
            }
        };
    }
}
