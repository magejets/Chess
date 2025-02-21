package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDao implements GameDao{
    static List<GameData> gameList = new ArrayList<>();

    public List<GameData> getGames() throws DataAccessException {
        return gameList;
    }

    // add game function
    // update game function
}
