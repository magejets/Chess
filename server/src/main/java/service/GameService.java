package service;

import dataaccess.DataAccessException;
import dataaccess.gamedao.MemoryGameDao;
import model.GameData;
import request.CreateRequest;
import request.ListRequest;
import result.CreateResult;
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

    public CreateResult createGame(CreateRequest request) {
        // authorize first

        GameData game = new GameData(request.getGameName());
        int gameID = -1; // initialized to a value it will never naturally be
        try {
            gameID = dataAccess.createGame(game);
        } catch (DataAccessException e) {

        }

        return new CreateResult(gameID);
    }
}
