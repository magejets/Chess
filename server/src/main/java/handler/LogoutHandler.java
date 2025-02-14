package handler;

import com.google.gson.Gson;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LogoutResult;
import result.RegisterResult;
import service.UserService;
import spark.Request;

public class LogoutHandler {
    Request req;
    public LogoutHandler(Request req) {
        this.req = req;
    }

    public String logout() {
        var request = new Gson().fromJson(req.headers("AuthToken"), LogoutRequest.class);
        var service = new UserService();
        return new Gson().toJson(service.logout(request), LogoutResult.class);
    }
}
