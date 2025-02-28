package dataaccess;

import chess.ChessGame;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.MemoryGameDao;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.util.ArrayList;
import java.util.List;

public class GameDaoTests {
    @Test
    public void testCreateAndGetGame() {
        ClearService clearService = new ClearService();
        clearService.clear();
        GameDao testDao = new MemoryGameDao();
        GameData gameData = new GameData("myGame");
        int gameID = -1;
        try {
            gameID = testDao.createGame(gameData);
        } catch (DataAccessException e) {

        }
        try {
            List<GameData> gotData = testDao.getGames();
            List<GameData> expected = new ArrayList<>();
            expected.add(new GameData(1, null, null, "myGame", new ChessGame()));
            Assertions.assertEquals(gotData.get(gameID - 1), expected.get(gameID - 1));
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testUpdateGame() {
        GameDao testDao = new MemoryGameDao();
        GameData gameData = new GameData("myGame");
        int gameID = -1;
        try {
            gameID = testDao.createGame(gameData);
        } catch (DataAccessException e) {

        }
        try {
            testDao.updateGame(gameID, "WHITE", "george");
            List<GameData> gotData = testDao.getGames();
            List<GameData> expected = new ArrayList<>();
            expected.add(new GameData(1, "george", null, "myGame", new ChessGame()));
            Assertions.assertEquals(gotData.get(gameID - 1), expected.get(gameID - 1));
        } catch (DataAccessException e) {

        }
    }
}
