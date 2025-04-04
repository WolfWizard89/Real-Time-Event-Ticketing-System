package org.coursework.w2052039.webConfig;

import org.coursework.w2052039.webConfig.LogWebSocketHandler;
import org.coursework.w2052039.webConfig.LoggerService;
import org.coursework.w2052039.webConfig.RemainTicketService;
import org.coursework.w2052039.webConfig.RemainWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private LoggerService loggerService;
    @Autowired
    private RemainTicketService remainTicketService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler(), "/logs")
                .setAllowedOrigins("http://localhost:3000");
        registry.addHandler(remainWebSocketHandler(), "/remain")
                .setAllowedOrigins("http://localhost:3000");
    }





    @Bean
    public LogWebSocketHandler logWebSocketHandler() {
        return new LogWebSocketHandler(loggerService);
    }

    @Bean
    public RemainWebSocketHandler remainWebSocketHandler() {
        return new RemainWebSocketHandler(remainTicketService);
    }
}