package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDao {
    public List<GameData> getGames() throws DataAccessException;

}
