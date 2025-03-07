package handler;


import request.LogoutRequest;
import result.LogoutResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private UserService service;

    public LogoutHandler(UserService userService) {
        this.service = userService;
    }

    public String handle(Request req, Response res) {
        var request = new LogoutRequest(req.headers("Authorization"));
        LogoutResult result = service.logout(request);
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
                yield "{}";
            }
        };
    }
}
