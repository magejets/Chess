package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;
import serverfacade.ServerFacade;

import java.util.ArrayList;
import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setup() {
        facade = new ServerFacade("http://localhost:" + port);
        try {
            facade.clear();
        } catch (ResponseException e) {

        }
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() {
        try {
            RegisterResult result = facade.register(new RegisterRequest("username", "password", "email"));
            Assertions.assertNull(result.message());
            Assertions.assertEquals(36, result.authToken().length());
            Assertions.assertEquals("username", result.username());
        } catch (ResponseException e) {
            Assertions.fail("Response exception");
        }
    }

    @Test
    public void registerNegative() {
        try {
            RegisterResult result = facade.register(new RegisterRequest("username", "password", null));
        } catch (ResponseException e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }
    }

    @Test
    public void loginPositive() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.logout(new LogoutRequest(registerResult.authToken()));
            LoginResult result = facade.login(new LoginRequest("username", "password"));
            Assertions.assertNull(result.message());
            Assertions.assertEquals(36, result.authToken().length());
            Assertions.assertEquals("username", result.username());
        } catch (ResponseException e) {
            Assertions.fail("Response exception");
        }
    }

    @Test
    public void loginNegative() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.logout(new LogoutRequest(registerResult.authToken()));
            LoginResult result = facade.login(new LoginRequest("username", "wrong-password"));
        } catch (ResponseException e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void createPositive() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            CreateResult result = facade.create(new CreateRequest(registerResult.authToken(), "testGame"));
            Assertions.assertEquals(1, result.gameID());
            ListResult gameList = facade.list(new ListRequest(registerResult.authToken()));
            Assertions.assertEquals(1, gameList.games().size());
            Assertions.assertEquals("testGame", gameList.games().getFirst().getGameName());
        } catch (ResponseException e) {
            Assertions.fail("Response exception");
        }
    }

    @Test
    public void createNegative() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            CreateResult result = facade.create(new CreateRequest(registerResult.authToken(), null));
        } catch (ResponseException e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }
    }

    @Test
    public void listPositive() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame2"));
            List<GameData> expected = new ArrayList<>();
            expected.add(new GameData(1, null, null, "testGame", new ChessGame()));
            expected.add(new GameData(2, null, null, "testGame2", new ChessGame()));
            ListResult result = facade.list(new ListRequest(registerResult.authToken()));
            Assertions.assertEquals(expected, result.games());
        } catch (ResponseException e) {
            Assertions.fail("Response exception");
        }
    }

    @Test
    public void listNegative() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame"));
            ListResult result = facade.list(new ListRequest("invalid auth token"));
        } catch (ResponseException e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void joinPositive() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame"));
            JoinResult result = facade.join(new JoinRequest(registerResult.authToken(), 1, "WHITE"));
            Assertions.assertNull(result.message());
            List<GameData> expected = new ArrayList<>();
            expected.add(new GameData(1, "username", null, "testGame", new ChessGame()));
            ListResult games = facade.list(new ListRequest(registerResult.authToken()));
            Assertions.assertEquals(expected, games.games());
        } catch (ResponseException e) {
            Assertions.fail("Response Error");
        }
    }

    @Test
    public void joinNegative() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame"));
            JoinResult result = facade.join(new JoinRequest(registerResult.authToken(), 1, "PURPLE"));
        } catch (ResponseException e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }
    }

    @Test
    public void logoutPositive() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            LogoutResult result = facade.logout(new LogoutRequest(registerResult.authToken()));
            Assertions.assertNull(result.message());
        } catch (ResponseException e) {
            Assertions.fail("Response Exception");
        }
    }

    @Test
    public void logoutNegative() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            LogoutResult result = facade.logout(new LogoutRequest("invalid auth token"));
        } catch (ResponseException e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void clearPositive() {
        try {
            RegisterResult registerResult = facade.register(new RegisterRequest("username", "password", "email"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame"));
            facade.create(new CreateRequest(registerResult.authToken(), "testGame2"));
            ClearResult result = facade.clear();
            registerResult = facade.register(new RegisterRequest("username", "password", "email")); // login again
            ListResult games = facade.list(new ListRequest(registerResult.authToken()));
            Assertions.assertEquals(0, games.games().size());
        } catch (ResponseException e) {
            Assertions.fail("Response exception");
        }
    }

}
