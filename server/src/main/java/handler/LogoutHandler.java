package handler;


import request.LogoutRequest;
import result.LogoutResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    public String handle(Request req, Response res) {
        //var request = new Gson().fromJson(req.headers("Authorization"), LogoutRequest.class);
        var request = new LogoutRequest(req.headers("Authorization"));
        var service = new UserService();
        LogoutResult result = service.logout(request);
        switch (result.message()) {
            case "Error: unauthorized":
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            case "Error: Data Access Exception":
                res.status(500);
                return "{ \"message\": \"Error: Data Access Exception\" }";
            case null, default:
                res.status(200);
                return "{}";
        }
    }
}
