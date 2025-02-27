package handler;

import result.RegisterResult;
import spark.*;
import com.google.gson.Gson;
import request.RegisterRequest;
import service.UserService;

public class RegisterHandler implements Route{
    public String handle(Request req, Response res) { // figure out the datatype
        var request = new Gson().fromJson(req.body(), RegisterRequest.class);
        var service = new UserService();
        RegisterResult result = service.register(request);
        switch (result.message()) {
            case "Error: already taken":
                res.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            case "Error: Data Access Exception":
                res.status(500);
                return "{ \"message\": \"Error: Data Access Exception\" }";
            case null, default:
                res.status(200);
                return new Gson().toJson(result, RegisterResult.class);
        }
    }
}
