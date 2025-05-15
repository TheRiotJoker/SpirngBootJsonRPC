package de.theriotjoker.bausteineverteiltersysteme_02.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CostTrackerWebSocketHandler costTrackerWebSocketHandler;

    public WebSocketConfig(CostTrackerWebSocketHandler handler) {
        costTrackerWebSocketHandler = handler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(costTrackerWebSocketHandler, "/costTrackSocket").setAllowedOrigins("*");
    }
}
