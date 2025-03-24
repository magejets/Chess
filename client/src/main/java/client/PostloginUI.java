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

import java.util.Arrays;
import java.util.List;

public class PostloginUI extends UI {
    private String userAuth;
    private GameData currentGame;

    public PostloginUI(String serverUrl) {
        super(serverUrl);
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
            case "create" -> EscapeSequences.SET_TEXT_COLOR_WHITE + create(params);
            case "list" -> EscapeSequences.SET_TEXT_COLOR_BLUE + list(params) + EscapeSequences.SET_TEXT_COLOR_WHITE;
            case "join" -> EscapeSequences.SET_TEXT_COLOR_WHITE + join(params);
            case "observe" -> EscapeSequences.SET_TEXT_COLOR_WHITE + observe(params);
            case "logout" -> EscapeSequences.SET_TEXT_COLOR_WHITE + logout();
            default -> EscapeSequences.SET_TEXT_COLOR_WHITE + help();
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
        CreateResult result = server.create(new CreateRequest(this.getUserAuth(), params[0]));
        return "Game: " + params[0] + " created";
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
        return returnString.toString();
    }

    private String join(String... params) throws ResponseException {
        List<GameData> gameList = server.list(new ListRequest(this.getUserAuth())).games();
        JoinResult result = server.join(new JoinRequest(this.getUserAuth(),
                gameList.get(Integer.parseInt(params[0]) - 1).getGameID(), params[1]));
        setCurrentGame(gameList.get(Integer.parseInt(params[0]) - 1));
        return "Joining as " + params[1] + " in game " + params[0] + ": " + getCurrentGame().getGameName();
    }

    private String observe(String... params) throws ResponseException {
        List<GameData> gameList = server.list(new ListRequest(this.getUserAuth())).games();
//        JoinResult result = server.join(new JoinRequest(this.getUserAuth(),
//                gameList.get(Integer.parseInt(params[0]) - 1).getGameID(), params[1]));
        setCurrentGame(gameList.get(Integer.parseInt(params[0]) - 1));
        return "Now observing game " + params[0] + ": " + getCurrentGame().getGameName();
    }

    private String logout() throws ResponseException {
        LogoutResult result = server.logout(new LogoutRequest(this.getUserAuth()));
        return "Successfully logged out";
    }
}
