package client;

import com.google.gson.Gson;
import ui.EscapeSequences;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final PreloginUI preClient;
    private final PostloginUI postClient;
    private final GameplayUI gameClient;
    private String phase;
    private WebSocketFacade wsFacade;
    private String url;

    public Repl(String url) {
        this.url = url;
        wsFacade =  new WebSocketFacade(url, this);
        preClient = new PreloginUI(url);
        postClient = new PostloginUI(url, wsFacade);
        gameClient = new GameplayUI(url, wsFacade);
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
            if (!result.equals("Making move")) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "\n[" + phase + "] >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
            }
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
                    //gameClient.setCurrentGame(postClient.getCurrentGame());
                    gameClient.setColor(result.substring(21,26)); // lucky that WHITE and BLACK have the same number of characters
                    //gameClient.drawBoard(gameClient.getColor(), null);
                    //gameClient.setAuthToken(postClient.getUserAuth());
                }
                if (result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Now observing game")) {
                    phase = "OBSERVE";
                    gameClient.setColor("OBSERVE");
                }
                if (result.equals(EscapeSequences.SET_TEXT_COLOR_WHITE + "leaving game")) {
                    phase = "LOGGED_IN";
                }
                if (result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Joining as") ||
                        result.startsWith(EscapeSequences.SET_TEXT_COLOR_WHITE + "Now observing game")) {
                    gameClient.setCurrentGame(postClient.getCurrentGame());
                    gameClient.setAuthToken(postClient.getUserAuth());
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + msg);
            }
        }
    }

    @Override
    public void notify(String notification) {
        ServerMessage genMessage = new Gson().fromJson(notification, ServerMessage.class);
        // switch case statement to see which type it is then deserialize(?) accordingly
        switch (genMessage.getServerMessageType()) {
            case NOTIFICATION:
                NotificationMessage notificationMessage = new Gson().fromJson(notification, NotificationMessage.class);
                System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + notificationMessage.getMessage());
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + "\n[" + phase + "] >>> "
                        + EscapeSequences.SET_TEXT_COLOR_GREEN);
                break;
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(notification, LoadGameMessage.class);
                gameClient.setCurrentGame(loadGameMessage.getGame());
                gameClient.drawBoard(gameClient.getColor(), null);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + "\n[" + phase + "] >>> "
                        + EscapeSequences.SET_TEXT_COLOR_GREEN);
                break;
            case ERROR:
                ErrorMessage errorMessage = new Gson().fromJson(notification, ErrorMessage.class);
                String printError = "ERROR";
                if (errorMessage.getMessage().equalsIgnoreCase("error: wrong turn")) {
                    printError = "It's not your turn!";
                } else if (errorMessage.getMessage().equalsIgnoreCase("error: invalid move")) {
                    printError = "Not a valid move. Try again!";
                } else if (errorMessage.getMessage().equalsIgnoreCase("error: game already over")) {
                    printError = "You cannot make moves after the game is over";
                }
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "\n" + printError +
                        EscapeSequences.SET_TEXT_COLOR_WHITE + "\n[" + phase + "] >>> "
                        + EscapeSequences.SET_TEXT_COLOR_GREEN);

        }
    }
}
