package dataaccess.gamedao;

import model.GameData;

import java.util.List;

public interface GameDao {
    public List<GameData> getGames();

}
