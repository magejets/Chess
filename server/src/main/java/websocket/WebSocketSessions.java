package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {
    public final ConcurrentHashMap<Integer, Set<Session>> sessions = new ConcurrentHashMap<>();

    public void addSessionToGame(int gameID, Session session) {
        sessions.putIfAbsent(gameID, new HashSet<>());
        sessions.get(gameID).add(session);
    }

    public void removeSessionFromGame(int gameID, Session session) {
        boolean removed = sessions.get(gameID).remove(session);
    }

    public void removeSession(Session session) {
        for (Map.Entry<Integer, Set<Session>> entry : sessions.entrySet()) {
            entry.getValue().remove(session);
        }
    }

    public Set<Session> getSessionsForGame(int gameID) {
        return sessions.get(gameID);
    }

    public void broadcast(int gameID, Session session, ServerMessage message) throws IOException {
            for (Session sesh : sessions.get(gameID)) {
                if (sesh.isOpen()) {
                    if (!sesh.equals(session)) { // identity equality I hope
                        sesh.getRemote().sendString(new Gson().toJson(message));
                    }
                }
            }

    }
}
