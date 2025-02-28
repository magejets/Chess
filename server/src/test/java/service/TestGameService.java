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
    public void TestAuthorizePositive() { // though it is for all the services we'll test it here in Game Service
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
    public void TestAuthorizeNegative() { // though it is for all the services we'll test it here in Game Service
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
    public void TestCreateGamePositive() {
        // setup
        ClearService clearService = new ClearService();
        clearService.clear();
        UserService userService = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        GameService service = new GameService();
        CreateRequest request = new CreateRequest(registerResult.authToken(), "myGame");
        GameData expectedGame = new GameData(0, "", "", "myGame", new ChessGame());
        ListRequest listRequest = new ListRequest(registerResult.authToken());

        // run the function
        CreateResult result = service.createGame(request);
        GameData actualGame = service.listGames(listRequest).getGames().getFirst();

        // test
        Assertions.assertEquals(0, result.gameID());
        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    public void TestCreateGameNegative() {
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
    public void TestCreateGameNegativeBadRequest() {
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
    public void TestListGamesPositive() {
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
        expectedGames.add(new GameData(0, "", "", "myGame", new ChessGame()));
        expectedGames.add(new GameData(1, "", "", "myGame2", new ChessGame()));

        // run the function
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        List<GameData> actualGames = service.listGames(listRequest).getGames();

        // test
        Assertions.assertEquals(0, result.gameID());
        Assertions.assertEquals(1, result2.gameID());
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    public void TestListGamesNegative() {
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
    public void TestJoinGamePositive() {
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
        expectedGames.add(new GameData(0, "username", "", "myGame", new ChessGame()));
        expectedGames.add(new GameData(1, "", "username", "myGame2", new ChessGame()));

        // run the function
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 0, "WHITE");
        JoinRequest joinRequest2 = new JoinRequest(registerResult.authToken(), 1, "BLACK");
        JoinResult result1 = service.joinGames(joinRequest);
        JoinResult result2 = service.joinGames(joinRequest2);

        // test
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        List<GameData> actualGames = service.listGames(listRequest).getGames();
        Assertions.assertEquals("", result1.message());
        Assertions.assertEquals("", result2.message());
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    public void TestJoinGameNegativeUnauthorized() {
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
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 0, "WHITE");
        JoinResult result1 = service.joinGames(joinRequest);

        // test
        ListRequest listRequest = new ListRequest(registerResult.authToken());
        List<GameData> actualGames = service.listGames(listRequest).getGames();
        Assertions.assertEquals("Error: unauthorized", result1.message());
    }

    @Test
    public void TestJoinGameNegativeBadColor() {
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
    public void TestJoinGameNegativeBadGameID() {
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
    public void TestJoinGameNegativeIDNoExist() {
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
    public void TestJoinGameNegativeTaken() {
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
        JoinRequest joinRequest  = new JoinRequest(registerResult.authToken(), 0, "WHITE");
        JoinResult result1 = service.joinGames(joinRequest);
        userService.logout(new LogoutRequest(registerResult.authToken()));
        registerRequest = new RegisterRequest("username1", "password", "email1@email.com");
        registerResult = userService.register(registerRequest);
        JoinRequest joinRequest2  = new JoinRequest(registerResult.authToken(), 0, "WHITE");
        JoinResult result2 = service.joinGames(joinRequest2);

        // test
        Assertions.assertEquals("Error: already taken", result2.message());
    }
}
