package service;

import dataaccess.DataAccessException;
import dataaccess.gamedao.MemoryGameDao;
import model.GameData;
import request.ListRequest;
import result.ListResult;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    final private MemoryGameDao dataAccess = new MemoryGameDao();

    public ListResult listGames(ListRequest request) {
        // authorize first

        List<GameData> gameList = new ArrayList<>();
        try {
            gameList = dataAccess.getGames();
        } catch (DataAccessException e) {

        }

        return new ListResult(gameList);
    }
}
