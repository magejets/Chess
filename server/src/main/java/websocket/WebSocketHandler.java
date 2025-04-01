package websocket;

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
    public void onClose(Session session) {

    }
    @OnWebSocketError
    public void onError(Throwable throwable) {

    }
    @OnWebSocketMessage
    public void onMessage(Session session, String str) {
        UserGameCommand message = new Gson.from
        // switch case statement to determine what to do
        switch (message) {
            case ""
        }
    }
}
