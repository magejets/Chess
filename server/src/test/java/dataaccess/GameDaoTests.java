package dataaccess;

import chess.ChessGame;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.SQLGameDao;
import dataaccess.userdao.SQLUserDao;
import dataaccess.userdao.UserDao;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class GameDaoTests {
    @BeforeAll
    public static void init() {
        UserDao initialize = new SQLUserDao(); // to initialize the database
    }
    // make a before each that does the clearing. just remove all duplicate code
    GameDao testDao = new SQLGameDao();
    GameData gameData = new GameData("myGame");
    int gameID;

    @BeforeEach
    public void testInit() {
        try {
            testDao.clear();
            gameID = testDao.createGame(new GameData("game"));
        } catch (DataAccessException e) {

        }
    }

    @Test
    public void testCreateAndGetGame() {
        try {
            GameData gotData = testDao.getGame(gameID);
            GameData expected = new GameData(1, null, null, "game", new ChessGame());
            Assertions.assertEquals(gotData, expected);
        } catch (DataAccessException e) {
            Assertions.fail("data access error");
        }
    }

    @Test
    public void testGetGames() {
        try {
            testDao.createGame(new GameData("game2"));
            List<GameData> gotData = testDao.getGames();
            List<GameData> expected = new ArrayList<>();
            expected.add(new GameData(1, null, null, "game", new ChessGame()));
            expected.add(new GameData(2, null, null, "game2", new ChessGame()));
            Assertions.assertEquals(gotData, expected);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateGame() {
        try {
            testDao.updateGame(gameID, "WHITE", "george");
            GameData gotData = testDao.getGame(gameID);
            GameData expected = new GameData(1, "george", null, "game", new ChessGame());
            Assertions.assertEquals(gotData, expected);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateNegative() {
        try {
            testDao.createGame(new GameData(0, null, "", null, new ChessGame()));
        } catch (DataAccessException e) {
            Assertions.assertEquals("Column 'gameName' cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetGamesNegative() {
        try {
            testDao.clear();
            List<GameData> gotGames = testDao.getGames(); // if no games are there then it should return an empty list
            Assertions.assertEquals(new ArrayList<GameData>(), gotGames);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testGetGameNegative() {
        try {
            GameData gotGame = testDao.getGame(2);
            Assertions.assertNull(gotGame);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateGameNegative() {
        try {
            testDao.updateGame(1, "Green", null);
        } catch (DataAccessException e) {
            Assertions.assertEquals("illegal color",e.getMessage());
        }
    }

    @Test
    public void testUpdateGameNegativeID() {
        try {
            int rowsUpdated = testDao.updateGame(2, "WHITE", "test"); // non existent game ID
            Assertions.assertEquals(0, rowsUpdated);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }


}
