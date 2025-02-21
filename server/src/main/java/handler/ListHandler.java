package handler;

import com.google.gson.Gson;
import request.ListRequest;
import result.ListResult;
import service.GameService;
import spark.Request;

public class ListHandler {
    Request req;

    public ListHandler(Request req) {
        this.req = req;
    }

    public String listGames() {
        var request = new Gson().fromJson(req.headers("AuthToken") , ListRequest.class);
        var service = new GameService();
        return new Gson().toJson(service.listGames(request), ListResult.class);
    }
}
