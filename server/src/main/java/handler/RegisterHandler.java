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
        res.status(200);
        return new Gson().toJson(service.register(request), RegisterResult.class);
    }
}
