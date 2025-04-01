package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessions = new WebSocketSessions();

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {

    }
    @OnWebSocketError
    public void onError(Throwable throwable) {

    }
    @OnWebSocketMessage
    public void onMessage(Session session, String str) {
        UserGameCommand message = new Gson().fromJson(str, UserGameCommand.class);
        // switch case statement to determine what to do

    }
}
