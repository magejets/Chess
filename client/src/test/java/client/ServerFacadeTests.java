package client;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.*;
import result.CreateResult;
import result.ListResult;
import result.LoginResult;
import result.RegisterResult;
import server.Server;
import server.ServerFacade;


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
    public void listPositive() {}

    @Test
    public void listNegative() {}

    @Test
    public void joinPositive() {}

    @Test
    public void joinNegative() {}

    @Test
    public void logoutPositive() {}

    @Test
    public void logoutNegative() {}

    @Test
    public void clearPositive() {}

}
