package handler;

import com.google.gson.Gson;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LogoutResult;
import result.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    public String handle(Request req, Response res) {
        //var request = new Gson().fromJson(req.headers("Authorization"), LogoutRequest.class);
        var request = new LogoutRequest(req.headers("Authorization"));
        var service = new UserService();
        // service.validate authtoken
        res.status(200);
        return new Gson().toJson(service.logout(request), LogoutResult.class);
    }
}
