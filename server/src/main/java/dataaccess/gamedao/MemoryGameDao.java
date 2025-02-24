package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDao implements GameDao{
    static HashMap<Integer, GameData> gameList = new HashMap<Integer, GameData>();
    static int lastIndex = 0;

    public List<GameData> getGames() throws DataAccessException {
        return new ArrayList<GameData>(gameList.values());
    }

    public int createGame(GameData game) throws DataAccessException{
        game.setGameID(lastIndex);
        gameList.put(lastIndex, game);
        return lastIndex++;
    }
}
