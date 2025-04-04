package org.coursework.w2052039.webConfig;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.CopyOnWriteArrayList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class LoggerService {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public synchronized void sendLog(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                System.out.println("Failed to send log: " + e.getMessage());
            }
        }
    }
}
}
