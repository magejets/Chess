package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.*;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;
import result.RegisterResult;

import java.util.ArrayList;
import java.util.List;

public class TestGameService {
    @Test
    public void testAuthorizePositive() { // though it is for all the services we'll test it here in Game Service
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();

        // run the function
        AuthData testData = null;
        try {
            testData = service.authorize(registerResult.authToken());
        } catch (DataAccessException e) {

        }

        // test
        Assertions.assertNotNull(testData);
    }

    @Test
    public void testAuthorizeNegative() { // though it is for all the services we'll test it here in Game Service
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        userService.logout(new LogoutRequest(registerResult.authToken())); // logout so the user is no longer authorized
        GameService service = new GameService();

        // run the function
        AuthData testData = null;
        try {
            testData = service.authorize(registerResult.authToken());
        } catch (DataAccessException e) {

        }

        // test
        Assertions.assertNull(testData);
    }

    @Test
    public void testCreateGamePositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        GameData expectedGame = new GameData(1, null, null, "myGame", new ChessGame());
        ListRequest listRequest = new ListRequest(registerResult.authToken());

        // run the function
        CreateResult result = service.createGame(request);
        GameData actualGame = service.listGames(listRequest).games().getFirst();

        // test
        Assertions.assertEquals(1, result.gameID());
        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    public void testCreateGameNegative() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        userService.logout(new LogoutRequest(registerResult.authToken())); // make them unauthorized
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");

        // run the function
        CreateResult result = service.createGame(request);

        // test
        Assertions.assertEquals("Error: unauthorized", result.message());
    }

    @Test
    public void testCreateGameNegativeBadRequest() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), ""); // no game name, bad request

        // run the function
        CreateResult result = service.createGame(request);

        // test
        Assertions.assertEquals("Error: bad request", result.message());
    }

    @Test
    public void testListGamesPositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();

        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        CreateResult result  = service.createGame(request);
        CreateResult result2 = service.createGame(request2);

        List<GameData> expectedGames = new ArrayList<>();
        expectedGames.add(new GameData(1, null, null, "myGame", new ChessGame()));
        expectedGames.add(new GameData(2, null, null, "myGame2", new ChessGame()));

        // run the function
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        List<GameData> actualGames = service.listGames(listRequest).games();

        // test
        Assertions.assertEquals(1, result.gameID());
        Assertions.assertEquals(2, result2.gameID());
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    public void testListGamesNegative() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        userService.logout(new LogoutRequest(registerResult.authToken()));
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        service.createGame(request);
        service.createGame(request2);

        // run the function
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        ListResult listResult = service.listGames(listRequest);

        // test
        Assertions.assertEquals("Error: unauthorized", listResult.getMessage());
    }

    @Test
    public void testJoinGamePositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        service.createGame(request);
        service.createGame(request2);

        List<GameData> expectedGames = new ArrayList<>();
        expectedGames.add(new GameData(1, "username", null, "myGame", new ChessGame()));
        expectedGames.add(new GameData(2, null, "username", "myGame2", new ChessGame()));

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 1, "WHITE");
        JoinRequest joinRequest2 = new JoinRequest(registerResult.authToken(), 2, "BLACK");
        JoinResult result1 = service.joinGames(joinRequest);
        JoinResult result2 = service.joinGames(joinRequest2);

        // test
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        List<GameData> actualGames = service.listGames(listRequest).games();
        Assertions.assertEquals("", result1.message());
        Assertions.assertEquals("", result2.message());
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    public void testJoinGameNegativeUnauthorized() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        userService.logout(new LogoutRequest(registerResult.authToken()));
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        service.createGame(request);
        service.createGame(request2);

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 1, "WHITE");
        JoinResult result1 = service.joinGames(joinRequest);

        // test
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        List<GameData> actualGames = service.listGames(listRequest).games();
        Assertions.assertEquals("Error: unauthorized", result1.message());
    }

    @Test
    public void testJoinGameNegativeBadColor() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        service.createGame(request);
        service.createGame(request2);

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 0, "GREEN");
        JoinResult result1 = service.joinGames(joinRequest);

        // test
        Assertions.assertEquals("Error: bad request", result1.message());
    }

    @Test
    public void testJoinGameNegativeBadGameID() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        service.createGame(request);
        service.createGame(request2);

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), -1, "WHITE");
        JoinResult result1 = service.joinGames(joinRequest);

        // test
        Assertions.assertEquals("Error: bad request", result1.message());
    }

    @Test
    public void testJoinGameNegativeIDNoExist() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        CreateRequest request2 = new CreateRequest(registerResult.authToken(), "myGame2");
        service.createGame(request);
        service.createGame(request2);

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 35, "WHITE");
        JoinResult result1 = service.joinGames(joinRequest);

        // test
        Assertions.assertEquals("Error: game does not exist", result1.message());
    }

    @Test
    public void testJoinGameNegativeTaken() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        service.createGame(request);

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 1, "WHITE");
        JoinResult result1 = service.joinGames(joinRequest);
        userService.logout(new LogoutRequest(registerResult.authToken()));
        registerRequest = new RegisterRequest("username1", "password", "email1@email.com");
        registerResult = userService.register(registerRequest);
        JoinRequest joinRequest2  = new JoinRequest(registerResult.authToken(), 1, "WHITE");
        JoinResult result2 = service.joinGames(joinRequest2);

        // test
        Assertions.assertEquals("Error: already taken", result2.message());
    }
}
