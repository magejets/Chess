package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private final PreloginUI preClient;
    private final PostloginUI postClient;
    private final GameplayUI gameClient;
    private String phase;

    public Repl(String url) {
        preClient = new PreloginUI(url);
        postClient = new PostloginUI(url);
        gameClient = new GameplayUI(url);
        phase = "LOGGED_OUT";
    }

    public void run() {
        System.out.println(EscapeSequences.RESET_ALL + EscapeSequences.WHITE_QUEEN + EscapeSequences.SET_TEXT_COLOR_WHITE
                + "Welcome to 240 chess. Type Help to get started."
                + EscapeSequences.WHITE_QUEEN);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        UI currentUI;
        while (!result.equals("quit")) {
            if (phase != null && (phase.equals("GAME") || phase.equals("OBSERVE"))) {
                gameClient.drawBoard(gameClient.getColor());
            }

            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + "\n[" + phase + "] >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
            String line = scanner.nextLine();
            currentUI = switch (phase) {
                case "LOGGED_OUT" -> preClient;
                case "LOGGED_IN" -> postClient;
                case null, default -> gameClient;
            };

            try {
                result = currentUI.eval(line);
                System.out.print(EscapeSequences.RESET_ALL + result);
                if (result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Logged in as")) {
                    phase = "LOGGED_IN";
                    postClient.setUserAuth(preClient.getUserAuth());
                }
                if (result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Successfully logged out")) {
                    phase = "LOGGED_OUT";
                    postClient.setUserAuth("");
                    preClient.setUserAuth("");
                }
                if (result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Joining as")) {
                    phase = "GAME";
                    gameClient.setCurrentGame(postClient.getCurrentGame());
                    gameClient.setColor(result.substring(21,26)); // lucky that WHITE and BLACK have the same number of characters
                }
                if (result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Now observing game")) {
                    phase = "OBSERVE";
                    gameClient.setCurrentGame(postClient.getCurrentGame());
                    gameClient.setColor("WHITE"); // observer sees from PoV white
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + msg);
            }
        }
    }
}
