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

    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException{
        GameData oldGame = gameList.get(gameID);
        GameData updatedGame = new GameData(gameID, playerColor.equals("WHITE") ? username : oldGame.getWhiteUsername(),
                playerColor.equals("BLACK") ? username : oldGame.getBlackUsername(),
                oldGame.getGameName(), oldGame.getGame());
        gameList.put(gameID, updatedGame);
    }
}
