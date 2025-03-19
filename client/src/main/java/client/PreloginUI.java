package client;

import exception.ResponseException;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import server.ServerFacade;
import ui.EscapeSequences;

import java.util.Arrays;

public class PreloginUI extends UI{
    private String userAuth;

    public PreloginUI(String serverUrl) {
        super(serverUrl);
        userAuth = "";
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserAuth() {
        return userAuth;
    }

    @Override
    public String eval(String input) throws ResponseException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "login" -> EscapeSequences.SET_TEXT_COLOR_WHITE + login(params);
            case "register" -> EscapeSequences.SET_TEXT_COLOR_WHITE + register(params);
            case "quit" -> "quit";
            default -> EscapeSequences.SET_TEXT_COLOR_WHITE + help();
        };
    }

    @Override
    public String help() {
        return EscapeSequences.RESET_ALL + EscapeSequences.SET_TEXT_COLOR_BLUE
                + "register <USERNAME> <PASSWORD> <EMAIL>" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - to create an account\n"
                + EscapeSequences.SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - to play chess\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - playing chess\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.SET_TEXT_COLOR_WHITE
                + " - with possible commands";
    }

    private String login(String... params) throws ResponseException{
        LoginResult result = server.login(new LoginRequest(params[0], params[1]));
        this.setUserAuth(result.authToken());
        return "Logged in as " + params[0];
    }

    private String register(String... params) throws ResponseException{
        RegisterResult result = server.register(new RegisterRequest(params[0], params[1], params[2]));
        return login(params);
    }
}
