package handler;

import com.google.gson.Gson;
import request.ListRequest;
import result.ListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListHandler implements Route {
    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.headers("Authorization") , ListRequest.class);
        var service = new GameService();
        res.status(200);
        return new Gson().toJson(service.listGames(request), ListResult.class);
    }
}
