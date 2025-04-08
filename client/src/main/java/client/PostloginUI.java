package client;

import exception.ResponseException;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import request.ListRequest;
import request.LogoutRequest;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;
import result.LogoutResult;
import ui.EscapeSequences;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.List;

public class PostloginUI extends UI {
    private String userAuth;
    private GameData currentGame;
    private WebSocketFacade wsFacade;

    public PostloginUI(String serverUrl, WebSocketFacade facade) {
        super(serverUrl);
        wsFacade = facade;
        userAuth = "";
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public GameData getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameData currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public String eval(String input) throws ResponseException{
        var tokens = input.split(" ");
        var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "create" -> create(params);
            case "list" ->list(params);// + EscapeSequences.SET_TEXT_COLOR_WHITE;
            case "join" -> join(params);
            case "observe" -> observe(params);
            case "logout" -> logout();
            default -> help();
        };
    }

    @Override
    public String help() {
        return EscapeSequences.RESET_ALL + EscapeSequences.SET_TEXT_COLOR_BLUE
                + "create <NAME>" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - a game\n"
                + EscapeSequences.SET_TEXT_COLOR_BLUE + "list" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - games\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - a game\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + "observe <ID>" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - a game\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + "logout" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - when you are done\n" + EscapeSequences.SET_TEXT_COLOR_BLUE+ "help" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - with possible commands";
    }

    private String create(String... params) throws ResponseException{
        if (params.length < 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Please include a game name";
        } else {
            CreateResult result = server.create(new CreateRequest(this.getUserAuth(), params[0]));
            return EscapeSequences.SET_TEXT_COLOR_WHITE + "Game: " + params[0] + " created";
        }

    }

    private String list(String... params) throws ResponseException  {
        ListResult result = server.list(new ListRequest(this.getUserAuth()));
        StringBuilder returnString = new StringBuilder("\tActive Games:\n");
        for (int i = 0; i < result.games().size(); i++) {
            GameData loopGame = result.games().get(i);
            returnString.append("\t\t" + (i + 1) + ") " +
                    " WHITE: " + (loopGame.getWhiteUsername() == null ? "<empty>" : loopGame.getWhiteUsername()) +
                    " BLACK: " + (loopGame.getBlackUsername() == null ? "<empty>" : loopGame.getBlackUsername()) +
                    " Name: " + loopGame.getGameName() +
                    (i == result.games().size() - 1 ? "" : "\n"));
        }
        return EscapeSequences.SET_TEXT_COLOR_BLUE + returnString.toString();
    }

    private String join(String... params) throws ResponseException {
        if (params.length < 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Please include the game id followed by your desired color";
        } else {
            String color = params[1].toUpperCase();
            List<GameData> gameList = server.list(new ListRequest(this.getUserAuth())).games();
            int gameID;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return EscapeSequences.SET_TEXT_COLOR_RED + params[0] + " is not a number, please enter a valid game ID";
            }
            if (gameList.size() < gameID) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Game ID not on list";
            } else if (gameID <= 0) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Game ID must be a positive integer";
            } else {
                if (!(color.equals("WHITE") || color.equals("BLACK"))) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Color must be WHITE or BLACK";
                }
                int gameListID = gameList.get(gameID - 1).getGameID();
                try {
                    JoinResult result = server.join(new JoinRequest(this.getUserAuth(),
                            gameListID, color));
                } catch (ResponseException e) {
                    if (e.getMessage().equals("Error: already taken")) {
                        return EscapeSequences.SET_TEXT_COLOR_RED + "Color already taken, choose another or select a different game";
                    }
                }
                setCurrentGame(gameList.get(gameID - 1));
                wsFacade.connect(this.getUserAuth(), gameListID, serverUrl);
                return EscapeSequences.SET_TEXT_COLOR_WHITE + "Joining as " + color + " in game " +
                        params[0] + ": " + getCurrentGame().getGameName();
            }
        }
    }

    private String observe(String... params) throws ResponseException {
        if (params.length < 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Please include the game id";
        } else {
            List<GameData> gameList = server.list(new ListRequest(this.getUserAuth())).games();
            int gameID;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return EscapeSequences.SET_TEXT_COLOR_RED + params[0] + " is not a number, please enter a valid game ID";
            }
            if (gameList.size() < gameID) {
                return EscapeSequences.SET_TEXT_COLOR_RED + "Game ID not on list";
            } else {
                int gameListID = gameList.get(gameID - 1).getGameID();
                setCurrentGame(gameList.get(Integer.parseInt(params[0]) - 1));
                wsFacade.connect(this.getUserAuth(), gameListID, serverUrl);
                return EscapeSequences.SET_TEXT_COLOR_WHITE + "Now observing game " + gameID + ": "
                        + getCurrentGame().getGameName();
            }
        }
    }

    private String logout() throws ResponseException {
        LogoutResult result = server.logout(new LogoutRequest(this.getUserAuth()));
        return EscapeSequences.SET_TEXT_COLOR_WHITE + "Successfully logged out";
    }
}
