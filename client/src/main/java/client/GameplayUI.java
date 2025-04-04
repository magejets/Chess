package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.EscapeSequences;
import websocket.WebSocketFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Arrays;


public class GameplayUI extends UI {
    private GameData currentGame;
    private String color;
    private WebSocketFacade wsFacade;
    private String authToken;

    public GameplayUI(String serverUrl, Repl notificationHandler) {
        super(serverUrl);
        wsFacade = new WebSocketFacade(serverUrl, notificationHandler);
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String eval(String input) {
        var tokens = input.split(" ");
        var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (this.getColor().equals("OBSERVE")) {
            return switch (cmd) {
                case "leave" -> leave();
                case "redraw" -> redraw();
                case "quit" -> "quit";
                default -> help();
            };
        } else {
            return switch (cmd) {
                case "leave" -> leave();
                case "redraw" -> redraw();
                case "move" -> move(params);
                case "highlight" -> highlight(params);
                case "resign" -> resign();
                case "quit" -> "quit";
                default -> help();
            };
        }
    }

    public void drawBoard(String color) {
        int startRow = 7;
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
            System.out.print("\n" + EscapeSequences.SET_BG_COLOR_BLUE + " " + EscapeSequences.SET_TEXT_COLOR_WHITE + (i + 1) + " ");
            for (int j = startCol; j != (startCol == 0 ? 8 : -1); j += (startCol == 0 ? 1 : -1)) {
                String squareBG = ((i % 2 == 1) && (j % 2 == 0)) || ((i % 2 == 0) && (j % 2 == 1)) ?
                        EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREEN;
                ChessPiece piece = currentGame.getGame().getBoard().getPiece(new ChessPosition(i + 1, j + 1));
                String pieceChar = getPieceChar(piece);
                System.out.print(EscapeSequences.RESET_ALL + squareBG + " " + pieceChar + " ");
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_BLUE + " " +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + (i + 1) + " " + EscapeSequences.RESET_BG_COLOR);
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

    private String leave() {
        return EscapeSequences.SET_TEXT_COLOR_WHITE + "leaving game"; // dummy function for now
    }

    private String redraw() {
        this.drawBoard(this.getColor());
        return "";
    }

    private String move(String... params) {
        // error handling for illegal input
        // turn the params into a move
        ChessPosition start = new ChessPosition(Integer.parseInt(String.valueOf(params[0].toLowerCase().charAt(1))),
                params[0].toLowerCase().charAt(0) - 'a' + 1);
        ChessPosition end = new ChessPosition(Integer.parseInt(String.valueOf(params[1].toLowerCase().charAt(1))),
                params[1].toLowerCase().charAt(0) - 'a' + 1);
        ChessPiece.PieceType promotion = null;
        if (params.length > 2) {
            switch (params[2].toUpperCase()) {
                case "QUEEN":
                    promotion = ChessPiece.PieceType.QUEEN;
                    break;
                case "ROOK":
                    promotion = ChessPiece.PieceType.ROOK;
                    break;
                case "BISHOP":
                    promotion = ChessPiece.PieceType.BISHOP;
                    break;
                case "KNIGHT":
                    promotion = ChessPiece.PieceType.KNIGHT;
                    break;
                default:
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Not a legal piece type";
            }
        }
        ChessMove move = new ChessMove(start, end, promotion);
        try {
            wsFacade.sendMessage(new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    this.getAuthToken(), this.getCurrentGame().getGameID(), move));
            return "Making move";
        } catch (IOException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage();
        }

    }

    private String highlight(String... params) {
        return "";
    }

    private String resign() {
        return "";
    }

    @Override
    public String help() {
        if (this.getColor().equals("OBSERVE")) {
            return EscapeSequences.RESET_ALL + EscapeSequences.SET_TEXT_COLOR_BLUE
                    + "redraw" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - redraws the chess board\n"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + "leave" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - the game\n"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + "help"
                    + EscapeSequences.SET_TEXT_COLOR_WHITE + " - with possible commands";
        } else {
            return EscapeSequences.RESET_ALL + EscapeSequences.SET_TEXT_COLOR_BLUE
                    + "redraw" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - redraws the chess board\n"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + "leave" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - the game\n"
                    + EscapeSequences.SET_TEXT_COLOR_BLUE + "move <start_column><start_row> <end_column><end_row> <promotion: optional>"
                    + EscapeSequences.SET_TEXT_COLOR_WHITE
                    + " - move a piece\n" + EscapeSequences.SET_TEXT_COLOR_BLUE
                    + "resign" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - the game\n" + EscapeSequences.SET_TEXT_COLOR_BLUE
                    + "highlight <column><row>" + EscapeSequences.SET_TEXT_COLOR_WHITE
                    + " - possible moves for indicated piece\n" + EscapeSequences.SET_TEXT_COLOR_BLUE+
                    "help" + EscapeSequences.SET_TEXT_COLOR_WHITE + " - with possible commands";
        }
    }
}
