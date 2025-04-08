package websocket;

import exception.ResponseException;
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
        this.session = session;
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
                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            //throw new ResponseException(500, ex.getMessage());
        }
    }

    public void sendMessage(UserGameCommand command) throws IOException{
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void connect(String authToken, int gameID, String url) throws ResponseException{
        try {
//            url = url.replace("http", "ws");
//            URI socketURI = new URI(url + "/ws");
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            this.session = container.connectToServer(this, socketURI);
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            sendMessage(command);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException{
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            sendMessage(command);
        } catch (IOException e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException{
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            sendMessage(command);
        } catch (IOException e) {
            throw new ResponseException(e.getMessage());
        }
    }
}
