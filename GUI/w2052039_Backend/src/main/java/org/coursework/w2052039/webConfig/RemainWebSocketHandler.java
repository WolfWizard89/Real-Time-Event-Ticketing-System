package org.coursework.w2052039.webConfig;



import org.coursework.w2052039.webConfig.RemainTicketService;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemainWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final RemainTicketService loggerService;

    public RemainWebSocketHandler(RemainTicketService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        loggerService.addSession(session);
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        loggerService.removeSession(session);
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Logic to handle text messages
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sessions.remove(session);
    }

    public void sendLog(String message) {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}