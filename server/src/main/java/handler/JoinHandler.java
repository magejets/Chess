package handler;

import com.google.gson.Gson;
import request.JoinRequest;
import result.JoinResult;
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
        return switch (result.message()) {
            case "Error: unauthorized" -> {
                res.status(401);
                yield "{ \"message\": \"Error: unauthorized\" }";
            }
            case "Error: already taken" -> {
                res.status(403);
                yield "{ \"message\": \"Error: already taken\" }";
            }
            case "Error: bad request" -> {
                res.status(400);
                yield "{ \"message\": \"Error: bad request\" }";
            }
            case "Error: game does not exist" -> {
                res.status(500);
                yield "{ \"message\": \"" + result.message() + "\" }";
            }
            case null, default -> {
                res.status(200);
                yield "{}";
            }
        };
    }
}
