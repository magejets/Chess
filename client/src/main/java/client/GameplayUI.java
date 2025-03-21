package client;

import model.GameData;

public class GameplayUI extends UI {
    private GameData currentGame;

    public GameplayUI(String serverUrl) {
        super(serverUrl);
    }

    public GameData getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameData currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public String eval(String input) {
//        try {
//            var tokens = input.split(" ");
//            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
//            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
//            return switch (cmd) {
//                case "signin" -> signIn(params);
//                case "rescue" -> rescuePet(params);
//                case "list" -> listPets();
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
//                case "quit" -> "quit";
//                default -> help();
//            };
//        } catch (ResponseException ex) {
//            return ex.getMessage();
//        }
        return "";
    }

    @Override
    public String help() {
        return "";
    }
}
