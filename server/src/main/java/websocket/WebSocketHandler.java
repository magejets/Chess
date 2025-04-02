package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessions = new WebSocketSessions();

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
            switch (message.getCommandType()) {
                case CONNECT:
                    sessions.addSessionToGame(message.getGameID(), session);
                    sessions.broadcast(message.getGameID(), new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, ""));
                    break;
                case LEAVE:
                    sessions.removeSessionFromGame(message.getGameID(), session);
                    sessions.broadcast(message.getGameID(), new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "left the game"));
                    break;
                case RESIGN:
                    // resign
                    sessions.removeSessionFromGame(message.getGameID(), session);
                    sessions.broadcast(message.getGameID(), new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "resigned the game"));
                    break;
                case MAKE_MOVE:
                    message = new Gson().fromJson(str, MakeMoveCommand.class);
                    // call repl to modify game? Service class? Access it from the sessions? WebAPI? DAO?
                    break;
            }
        } catch (Exception e) {

        }

    }
}
