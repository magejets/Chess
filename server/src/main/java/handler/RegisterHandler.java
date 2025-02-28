package handler;

import result.RegisterResult;
import spark.*;
import com.google.gson.Gson;
import request.RegisterRequest;
import service.UserService;

public class RegisterHandler implements Route{
    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.body(), RegisterRequest.class);
        var service = new UserService();
        RegisterResult result = service.register(request);
        return switch (result.message()) {
            case "Error: already taken" -> {
                res.status(403);
                yield "{ \"message\": \"Error: already taken\" }";
            }
            case "Error: Data Access Exception" -> {
                res.status(500);
                yield "{ \"message\": \"Error: Data Access Exception\" }";
            }
            case "Error: bad request" -> {
                res.status(400);
                yield "{ \"message\": \"Error: bad request\" }";
            }
            case null, default -> {
                res.status(200);
                yield new Gson().toJson(result, RegisterResult.class);
            }
        };
    }
}
