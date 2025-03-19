package client;

import ui.EscapeSequences;

import java.util.Arrays;

public class PostloginUI extends UI {
    public PostloginUI(String serverUrl) {
        super(serverUrl);
    }

    @Override
    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "create" -> EscapeSequences.SET_TEXT_COLOR_WHITE + create(params);
            case "list" -> EscapeSequences.SET_TEXT_COLOR_WHITE + list(params);
            case "join" -> EscapeSequences.SET_TEXT_COLOR_WHITE + join(params);
            case "observe" -> EscapeSequences.SET_TEXT_COLOR_WHITE + observe(params);
            case "logout" -> EscapeSequences.SET_TEXT_COLOR_WHITE + logout(params);
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

    private String create() {
        return "";
    }

    private String list() {
        return "";
    }

    private String join() {
        return "";
    }

    private String observe() {
        return "";
    }

    private String logout() {
        return "";
    }
}
