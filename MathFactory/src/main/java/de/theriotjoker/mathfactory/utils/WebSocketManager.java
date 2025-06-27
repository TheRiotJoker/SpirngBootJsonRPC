package de.theriotjoker.mathfactory.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.theriotjoker.mathfactory.InstanceId;
import de.theriotjoker.mathfactory.dto.request.WebSocketPayload;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class WebSocketManager {

    private final String dataManagerUri = "ws://datamanager:8081/dataManager";
    private final ObjectMapper mapper = new ObjectMapper();
    private final StandardWebSocketClient client = new StandardWebSocketClient();
    private final InstanceId instanceId;
    private final AtomicReference<WebSocketSession> sessionRef = new AtomicReference<>();

    public WebSocketManager(InstanceId instanceId) {
        this.instanceId = instanceId;
    }

    @PostConstruct
    public void connect() {
        try {
            client.doHandshake(new AbstractWebSocketHandler() {
                @Override
                public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                    sessionRef.set(session);

                    WebSocketPayload payload = WebSocketPayload.builder()
                            .type(WsMessageType.REGISTER.name())
                            .uuid(instanceId.getUuid().toString())
                            .payload("registration")
                            .build();

                    session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
                    log.info("Sent REGISTER to DataManager: {}", payload);
                }

                @Override
                public void handleTransportError(WebSocketSession session, Throwable exception) {
                    log.error("WebSocket transport error: {}", exception.getMessage(), exception);
                    sessionRef.set(null);
                }

                @Override
                public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
                    log.warn("WebSocket closed: {}", status);
                    sessionRef.set(null);
                }

            }, dataManagerUri).get();
        } catch (Exception e) {
            log.error("Failed to register with DataManager: {}", e.getMessage());
            throw new IllegalStateException("Cannot proceed without WebSocketRegistration");
        }
    }

    public void sendMessageToDataManager(WsMessageType type, String payloadContent) {
        WebSocketSession session = sessionRef.get();
        if (session != null && session.isOpen()) {
            try {
                WebSocketPayload payload = WebSocketPayload.builder()
                        .type(type.name())
                        .uuid(instanceId.getUuid().toString())
                        .payload(payloadContent)
                        .build();

                session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
                log.info("Sent message to DataManager: {}", payload);

            } catch (Exception e) {
                log.error("Failed to send message to DataManager: {}", e.getMessage(), e);
            }
        } else {
            log.warn("WebSocket session not ready. Message to DataManager not sent.");
        }
    }
}
