package de.theriotjoker.datamanager.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.theriotjoker.datamanager.dto.request.WebSocketPayload;
import de.theriotjoker.datamanager.service.MathInstanceService;
import de.theriotjoker.datamanager.utils.WsMessageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
@Slf4j
public class MathFactoryInstanceHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> uuidSessionMap = new ConcurrentHashMap<>();
    private final MathInstanceService mathInstanceService;
    private final CostTrackerWebSocketHandler costTrackerWebSocketHandler;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        uuidSessionMap.values().remove(session);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String stringPayload = message.getPayload();

        WebSocketPayload load = mapper.readValue(stringPayload, WebSocketPayload.class);


        if(Objects.isNull(load.getUuid()) || Objects.isNull(load.getType())) {
            return;
        }

        WsMessageType type = WsMessageType.valueOf(load.getType());

        if(type == WsMessageType.REGISTER) {
            if(Objects.nonNull(load.getPayload())) {
                String password = (String) load.getPayload();
                if(password.contentEquals("registration")) {
                    uuidSessionMap.putIfAbsent(load.getUuid(), session);
                    log.info("A new Math Instance just registered. Welcome: {}",load.getUuid());
                    return;
                }
            }
        }

        switch(type) {
            case LIST_INSTANCES -> session.sendMessage(new TextMessage(mapper.writeValueAsString(getAllConnectedFactories())));
            case INSTANCE_COST_ENQUIRY -> session.sendMessage(new TextMessage(mapper.writeValueAsString(mathInstanceService.getAll(getAllConnectedFactories()))));
            case CHECK_THRESHOLD -> checkThreshold(load);
        }
    }

    private void checkThreshold(WebSocketPayload load) {
        if(Objects.isNull(load.getPayload())) {
            return;
        }
        String payload;
        try {
            payload = (String) load.getPayload();
            UUID.fromString(payload);
        } catch (Exception e) {
            return;
        }

        costTrackerWebSocketHandler.informIfThresholdIsReached(payload);
    }

    public List<String> getAllConnectedFactories() {
        return Collections.list(uuidSessionMap.keys());
    }
}