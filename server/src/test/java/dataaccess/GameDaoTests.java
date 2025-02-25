package dataaccess;

import chess.ChessGame;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.MemoryGameDao;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class GameDaoTests {
    @Test
    public void testCreateAndGetGame() {
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
            expected.add(new GameData(0, "", "", "myGame", new ChessGame()));
            Assertions.assertTrue(gotData.get(gameID).equals(expected.get(gameID)));
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
            expected.add(new GameData(0, "george", "", "myGame", new ChessGame()));
            Assertions.assertTrue(gotData.get(gameID).equals(expected.get(gameID)));
        } catch (DataAccessException e) {

        }
    }
}
