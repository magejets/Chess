package client;

import exception.ResponseException;
import serverfacade.ServerFacade; // should the server facade be in shared?

public abstract class UI {
    protected String serverUrl;
    protected ServerFacade server;

    public UI(String serverUrl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
    }

    public abstract String eval(String input) throws ResponseException;

    public abstract String help();
}
