package service;

import dataaccess.gamedao.MemoryGameDao;
import model.GameData;
import request.ListRequest;
import result.ListResult;

import java.util.List;

public class GameService {
    final private MemoryGameDao dataAccess = new MemoryGameDao();

    public ListResult listGames(ListRequest request) {
        // authorize first

        List<GameData> gameList = dataAccess.getGames();

        return new ListResult(gameList);
    }
}
