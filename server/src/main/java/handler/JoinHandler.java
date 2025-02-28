package handler;

import com.google.gson.Gson;
import request.JoinRequest;
import request.ListRequest;
import result.JoinResult;
import result.ListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinHandler implements Route {
    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.body(), JoinRequest.class);
        request.setAuthToken(req.headers("Authorization"));
        var service = new GameService();
        JoinResult result = service.joinGames(request);
        switch (result.message()) {
            case "Error: unauthorized":
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            case "Error: already taken":
                res.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            case "Error: bad request":
                res.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            case "Error: game does not exist":
                res.status(500);
                return "{ \"message\": \"" + result.message() + "\" }";
            case null, default:
                res.status(200);
                return new Gson().toJson(result, JoinResult.class);
        }
    }
}
