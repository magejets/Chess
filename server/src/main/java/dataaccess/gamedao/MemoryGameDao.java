package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDao implements GameDao{
    static HashMap<Integer, GameData> gameList = new HashMap<>();
    static int lastIndex = 1;

    public List<GameData> getGames() throws DataAccessException {
        return new ArrayList<>(gameList.values());
    }

    public int createGame(GameData game) throws DataAccessException{
        game.setGameID(lastIndex);
        gameList.put(lastIndex, game);
        return lastIndex++;
    }

    public boolean updateGame(int gameID, String playerColor, String username) throws DataAccessException{
        GameData oldGame = gameList.get(gameID);
        if (oldGame == null) {
            throw new DataAccessException("Error: game does not exist");
        }
        GameData updatedGame;
        if (playerColor.equals("WHITE") ?
                (oldGame.getWhiteUsername() == null) : (oldGame.getBlackUsername() == null)) {
             updatedGame = new GameData(gameID, playerColor.equals("WHITE") ? username : oldGame.getWhiteUsername(),
                    playerColor.equals("BLACK") ? username : oldGame.getBlackUsername(),
                    oldGame.getGameName(), oldGame.getGame());
            gameList.put(gameID, updatedGame);
            return true;
        } else {
            return false;
        }

    }

    public void clear() throws DataAccessException {
        gameList.clear();
        lastIndex = 1;
    }
}
