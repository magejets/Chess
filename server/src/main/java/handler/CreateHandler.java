package handler;

import com.google.gson.Gson;
import request.CreateRequest;
import result.CreateResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.body() , CreateRequest.class);
        request.setAuthToken(req.headers("Authorization"));
        var service = new GameService();
        res.status(200);
        return new Gson().toJson(service.createGame(request), CreateResult.class);
    }
}
