package dataaccess.gamedao;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDao implements GameDao{
    static List<GameData> gameList = new ArrayList<>();

    public List<GameData> getGames() {
        return gameList;
    }

    // add game function
    // update game function
}
