package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.EscapeSequences;

public class GameplayUI extends UI {
    private GameData currentGame;
    private String color;

    public GameplayUI(String serverUrl) {
        super(serverUrl);
    }

    public GameData getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameData currentGame) {
        this.currentGame = currentGame;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public void drawBoard(String color) {
        int startRow = 7; // default to WHITE values in case some other value makes it into here
        int startCol = 0;

        if (color.equals("BLACK")) { // if not white then change it
                startRow = 0;
                startCol = 7;
        }

        // print the header
        if (startCol == 0) {
            System.out.print(EscapeSequences.RESET_ALL + "\n\n" +
                    EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_WHITE +
                    "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET_ALL);
        } else {
            System.out.print(EscapeSequences.RESET_ALL + "\n\n" +
                    EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_WHITE +
                    "    h  g  f  e  d  c  b  a    " + EscapeSequences.RESET_ALL);
        }

        // loop though the board and print the side label and the squares
        for (int i = startRow; i != (startRow == 7 ? -1 : 8); i += (startRow == 7 ? -1 : 1)) {
            // print the side label
            System.out.print("\n" + EscapeSequences.SET_BG_COLOR_BLUE + " " + EscapeSequences.SET_TEXT_COLOR_WHITE + i + " ");
            for (int j = startCol; j != (startCol == 0 ? 8 : -1); j += (startCol == 0 ? 1 : -1)) {
                String squareBG = ((i % 2 == 1) && (j % 2 == 0)) || ((i % 2 == 0) && (j % 2 == 1)) ?
                        EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREEN;
                ChessPiece piece = currentGame.getGame().getBoard().getPiece(new ChessPosition(i + 1, j + 1));
                String pieceChar = getPieceChar(piece);
                System.out.print(EscapeSequences.RESET_ALL + squareBG + " " + pieceChar + " ");
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_BLUE + " " +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + i + " " + EscapeSequences.RESET_BG_COLOR);
        }

        // print the footer
        if (startCol == 0) {
            System.out.print("\n" + EscapeSequences.RESET_ALL +
                    EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_WHITE +
                    "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET_ALL + "\n");
        } else {
            System.out.print("\n" + EscapeSequences.RESET_ALL +
                    EscapeSequences.SET_BG_COLOR_BLUE + EscapeSequences.SET_TEXT_COLOR_WHITE +
                    "    h  g  f  e  d  c  b  a    " + EscapeSequences.RESET_ALL + "\n");
        }
    }

    private static String getPieceChar(ChessPiece piece) {
        String pieceChar;
        if (piece == null) {
            pieceChar = " ";
        } else {
            pieceChar = (piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    EscapeSequences.SET_TEXT_COLOR_WHITE : EscapeSequences.SET_TEXT_COLOR_BLACK) +
                    switch (piece.getPieceType()) {
                        case QUEEN -> "Q";
                        case KING -> "K";
                        case ROOK -> "R";
                        case BISHOP -> "B";
                        case KNIGHT -> "N";
                        case PAWN -> "P";
                        case null, default -> "";
                    };
        }
        return pieceChar;
    }

    @Override
    public String help() {
        return "";
    }
}
