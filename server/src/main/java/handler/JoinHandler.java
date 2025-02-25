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
        res.status(200);
        return new Gson().toJson(service.joinGames(request), JoinResult.class);
    }
}
