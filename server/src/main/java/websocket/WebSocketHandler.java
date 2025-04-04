package websocket;

import chess.ChessGame;
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
            ChessGame.TeamColor turn = service.getTurn(userName, message.getGameID());
            switch (message.getCommandType()) {
                case CONNECT:
                    sessions.addSessionToGame(message.getGameID(), session);
                    sessions.broadcast(message.getGameID(), session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    userName + " has joined"));
                    break;
                case LEAVE:
                    sessions.removeSessionFromGame(message.getGameID(), session);
                    sessions.broadcast(message.getGameID(), session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    userName + "left the game"));
                    break;
                case RESIGN:
                    // resign
                    sessions.removeSessionFromGame(message.getGameID(), session);
                    // service.resign(...)
                    sessions.broadcast(message.getGameID(), session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    userName + "resigned the game"));
                    break;
                case MAKE_MOVE:
                    MakeMoveCommand moveCommand = new Gson().fromJson(str, MakeMoveCommand.class);
                    GameData updatedGame = service.makeMove(message.getGameID(), moveCommand.getMove(), turn);
                    // service.checkGameState to see if it's checkmate or something, then broadcast a win screen
                    sessions.broadcast(message.getGameID(), null,
                            new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame));
                    break;
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Wrong turn")) {
                try {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Wrong turn");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (Exception ex) {

                }
            }
        }

    }
}
