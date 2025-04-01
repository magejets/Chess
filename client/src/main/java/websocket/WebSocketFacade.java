package websocket;

import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.google.gson.Gson;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    // if I extend server message do some sort of deserialization
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            //throw new ResponseException(500, ex.getMessage());
        }
    }

    public void connect(String authToken, int gameID) {
        var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);

    }
}
