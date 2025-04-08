package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.authdao.AuthDao;
import dataaccess.gamedao.GameDao;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessions = new WebSocketSessions();
    private WebSocketService service;

    public WebSocketHandler(AuthDao authDao, GameDao gameDao) {
        this.service = new WebSocketService(authDao, gameDao);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.removeSession(session);
    }
    @OnWebSocketError
    public void onError(Throwable throwable) {
        // find a way to tell the client to print an error
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String str) {
        UserGameCommand message = new Gson().fromJson(str, UserGameCommand.class);
        // switch case statement to determine what to do
        try {
            String userName = service.getUsername(message.getAuthToken());
            boolean authorized = service.validateAuth(message.getAuthToken());
            if (!authorized) {
                session.getRemote().sendString(new Gson().toJson(
                        new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "error: unauthorized")));
            } else {
                ChessGame.TeamColor turn;
                switch (message.getCommandType()) {
                    case CONNECT:
                        connect(message, userName, session);
                        break;
                    case LEAVE:
                        turn = service.getTurn(userName, message.getGameID());
                        sessions.removeSessionFromGame(message.getGameID(), session);
                        service.leave(message.getGameID(), userName, turn);
                        sessions.broadcast(message.getGameID(), session,
                                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        userName + " left the game"));
                        break;
                    case RESIGN:
                        // resign
                        turn = service.getTurn(userName, message.getGameID());
                        sessions.removeSessionFromGame(message.getGameID(), session);
                        service.resign(message.getGameID(), turn);
                        sessions.broadcast(message.getGameID(), session,
                                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        userName + " (" + turn.toString() + ") resigned the game"));
                        session.getRemote().sendString(new Gson().toJson(
                                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "You resigned the game")));
                        break;
                    case MAKE_MOVE:
                        turn = service.getTurn(userName, message.getGameID());
                        makeMove(str, turn, session, userName);
                        break;
                }
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Wrong turn")) {
                try {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Wrong turn");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (Exception ex) {

                }
            }
            if (e.getMessage().equals("Not a valid move")) {
                try {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid move");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (Exception ex) {

                }
            }
            if (e.getMessage().equals("Game over")) {
                try {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game already over");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (Exception ex) {

                }
            }
            if (e.getMessage().equals("Observer can't resign")) {
                try {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: observer can't resign");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (Exception ex) {

                }
            }
        }

    }

    private void makeMove(String jsonCommand, ChessGame.TeamColor turn, Session session, String userName) throws Exception{
        UserGameCommand message = new Gson().fromJson(jsonCommand, UserGameCommand.class);
        MakeMoveCommand moveCommand = new Gson().fromJson(jsonCommand, MakeMoveCommand.class);
        GameData updatedGame = service.makeMove(message.getGameID(), moveCommand.getMove(), turn);

        sessions.broadcast(message.getGameID(), null,
                new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame));
        ChessMove move = moveCommand.getMove();
        char startRow = (char) ((move.getStartPosition().getColumn() - 1) + 'a');
        char endRow = (char) ((move.getEndPosition().getColumn() - 1) + 'a');
        String moveString = "" + startRow + move.getStartPosition().getRow() + " to " +
                endRow + move.getEndPosition().getRow();
        sessions.broadcast(message.getGameID(), session,
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        userName + " made move " + moveString));
        if (updatedGame.getGame().getWinner() != ChessGame.Winner.NONE_YET) {
            String endGameMessage;
            if (updatedGame.getGame().getWinner() == ChessGame.Winner.STALEMATE) {
                endGameMessage = "TIE BY STALEMATE";
            } else {
                endGameMessage = updatedGame.getGame().getWinner().toString() +
                        " (" + userName + ") HAS WON THE GAME by Checkmate";
            }
            sessions.broadcast(message.getGameID(), null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            endGameMessage));
        } else {
            if (updatedGame.getGame().isInCheck(ChessGame.TeamColor.WHITE)) {
                sessions.broadcast(message.getGameID(), null,
                        new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                updatedGame.getWhiteUsername() + " (WHITE) is in check"));
            } else if (updatedGame.getGame().isInCheck(ChessGame.TeamColor.BLACK)) {
                sessions.broadcast(message.getGameID(), null,
                        new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                updatedGame.getBlackUsername() + " (BLACK) is in check"));
            }
        }
    }

    private void connect(UserGameCommand message, String userName, Session session) throws Exception{
        // check if the game exists
        GameData ifExists = service.getGame(message.getGameID());
        // validate the auth token


        if (ifExists != null) {
            sessions.addSessionToGame(message.getGameID(), session);

            String userRole;
            if (userName.equals(ifExists.getWhiteUsername())) {
                userRole = "WHITE";
            } else if (userName.equals(ifExists.getBlackUsername())) {
                userRole = "BLACK";
            } else {
                userRole = "OBSERVER";
            }

            // send notification to all other users
            sessions.broadcast(message.getGameID(), session,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            userName + " has joined as " + userRole));
            // send load game to this user
            session.getRemote().sendString(new Gson().toJson(
                    new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                            service.getGame(message.getGameID()))));
        } else {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "error: bad game id")));
        }
    }
}
