package de.theriotjoker.datamanager.websocket;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final CostTrackerWebSocketHandler costTrackerWebSocketHandler;
    private final MathFactoryInstanceHandler mathFactoryInstanceHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(costTrackerWebSocketHandler, "/costTrackSocket").setAllowedOrigins("*");
        registry.addHandler(mathFactoryInstanceHandler, "/dataManager").setAllowedOrigins("*");
    }
}
