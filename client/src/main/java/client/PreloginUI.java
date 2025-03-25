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
        var tokens = input.split(" ");
        var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "login" ->  login(params);
            case "register" -> register(params);
            case "quit" -> "quit";
            default -> help();
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
        if (params.length < 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Please include your username and password";
        } else if (params.length > 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Please include" + EscapeSequences.SET_TEXT_BOLD +
                    EscapeSequences.SET_TEXT_ITALIC + " only " + EscapeSequences.RESET_TEXT_ITALIC +
                    EscapeSequences.RESET_TEXT_BOLD_FAINT + "your username and password (no email)";
        } else {
            try {
                LoginResult result = server.login(new LoginRequest(params[0], params[1]));
                this.setUserAuth(result.authToken());
                return EscapeSequences.SET_TEXT_COLOR_WHITE + "Logged in as " + params[0];
            } catch (Exception e) {
                if (e.getMessage().equals("Error: unauthorized")) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Username or password incorrect";
                } else {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Error, please try again";
                }
            }
        }
    }

    private String register(String... params) throws ResponseException{
        if (params.length < 3) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Please include your username, password, and email";
        } else {
            try {
                RegisterResult result = server.register(new RegisterRequest(params[0], params[1], params[2]));
                return login(params);
            } catch (ResponseException e) {
                if (e.getMessage().equals("Error: already taken")) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Username already taken, select another";
                } else {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Error, please try again";
                }
            }
        }
    }
}
