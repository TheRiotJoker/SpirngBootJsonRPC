package de.theriotjoker.bausteineverteiltersysteme_02.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.request.ChangeCostThresholdDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.CostTrackResponseDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.service.CostTrackerService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CostTrackerWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> socketSessionMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, String> uuidSessionMap = new ConcurrentHashMap<>();
    private final CostTrackerService costTrackerService;
    private static final ObjectMapper mapper = new ObjectMapper();

    public CostTrackerWebSocketHandler(CostTrackerService service) {
        this.costTrackerService = service;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        socketSessionMap.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        socketSessionMap.remove(session.getId());
        try {
            uuidSessionMap.values().remove(session.getId());
        } catch (NullPointerException ignored) {}
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String stringPayload = message.getPayload();

        ChangeCostThresholdDTO payload;

        try {
            payload = mapper.readValue(stringPayload, ChangeCostThresholdDTO.class);

            if(payload.getUuid() == null || payload.getNewThreshold() == null) {
                throw new IllegalArgumentException("No field in the Cost Update Object may be null.");
            }

            Integer newThreshold = costTrackerService.updateThreshold(payload.getUuid(), payload.getNewThreshold());

            CostTrackResponseDTO dto = CostTrackResponseDTO.builder()
                    .status("success")
                    .message("Threshold successfully updated. New threshold: "+newThreshold)
                    .build();

            uuidSessionMap.putIfAbsent(payload.getUuid(), session.getId());

            session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        } catch (JsonProcessingException ex) {
            sendError(session, "Invalid payload sent.");
        } catch (IllegalArgumentException ex) {
            sendError(session, ex.getMessage());
        }
    }

    public static void sendMessageTo(String uuid, CostTrackResponseDTO dto) {
        if(!uuidSessionMap.containsKey(uuid)) {
            return;
        }
        String sessionId = uuidSessionMap.get(uuid);
        WebSocketSession session = socketSessionMap.get(sessionId);
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        } catch (IOException ignored) {}
    }

    private void sendError(WebSocketSession session, String message) throws IOException {
        CostTrackResponseDTO dto = CostTrackResponseDTO.builder()
                .status("error")
                .message(message)
                .build();
        session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
    }
}
