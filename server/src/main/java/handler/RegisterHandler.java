package handler;

import result.RegisterResult;
import spark.*;
import com.google.gson.Gson;
import request.RegisterRequest;
import service.UserService;

public class RegisterHandler {
    Request req;
    public RegisterHandler(Request req) {
        this.req = req;
    }

    public String register() { // figure out the datatype
        var request = new Gson().fromJson(req.body(), RegisterRequest.class);
        var service = new UserService();
        return new Gson().toJson(service.register(request), RegisterResult.class);
    }
}
