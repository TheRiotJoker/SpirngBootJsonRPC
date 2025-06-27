package de.theriotjoker.datamanager.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.theriotjoker.datamanager.dto.request.WebSocketPayload;
import de.theriotjoker.datamanager.dto.response.CostTrackResponseDTO;
import de.theriotjoker.datamanager.dto.response.CustomerTotalUsageDTO;
import de.theriotjoker.datamanager.dto.response.ThresholdInformationDTO;
import de.theriotjoker.datamanager.service.CustomerService;
import de.theriotjoker.datamanager.utils.WsMessageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static de.theriotjoker.datamanager.utils.WsMessageType.CHECK_THRESHOLD;

@Component
@AllArgsConstructor
@Slf4j
public class CostTrackerWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> socketSessionMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, String> uuidSessionMap = new ConcurrentHashMap<>();
    private final CustomerService customerService;
    private static final ObjectMapper mapper = new ObjectMapper();

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


        WebSocketPayload load = mapper.readValue(stringPayload, WebSocketPayload.class);


        if(Objects.isNull(load.getUuid()) || Objects.isNull(load.getType())) {
            return;
        }

        WsMessageType type = WsMessageType.valueOf(load.getType());

        if(!type.equals(CHECK_THRESHOLD)) {
            uuidSessionMap.putIfAbsent(load.getUuid(), session.getId());
        }

        log.info("Message arrived: {}", load.getType());
        log.info("For customer: {}", load.getUuid());
        log.info("The entry in the uuidSessionMap for the customer is: {}", uuidSessionMap.get(load.getUuid()));

        switch(type) {
            case COST_THRESHOLD_CHANGE -> changeCostThreshold(UUID.fromString(load.getUuid()), Integer.valueOf((String) load.getPayload()));
            case CUSTOMER_COST_ENQUIRY -> getCostForCustomer(load.getUuid());
            case CHECK_THRESHOLD -> informIfThresholdIsReached(load.getUuid());
            case REGISTER -> sendMessage(load.getUuid(), "Registered.");
            default -> sendError(load.getUuid(), "Message unreadable.");
        }
    }

    private static WebSocketSession getWebSocketSessionForUuid(String uuid) {
        if(!uuidSessionMap.containsKey(uuid)) {
            throw new IllegalArgumentException("Connection may have been closed.");
        }
        String sessionId = uuidSessionMap.get(uuid);
        return socketSessionMap.get(sessionId);
    }


    private static void sendMessage(String uuid, String message) {
        try {
            WebSocketSession session = getWebSocketSessionForUuid(uuid);
            session.sendMessage(new TextMessage(message));
        } catch (IOException ignored) {}
    }

    public void informIfThresholdIsReached(String uuid) {

        log.info("Asked if user: {} has reached their threshold.", uuid);

        ThresholdInformationDTO thresholdInformation = customerService.getThresholdInformation(UUID.fromString(uuid));

        if(thresholdInformation.isExceeded()) {
            sendMessage(uuid, "THRESHOLD EXCEEDED: "+thresholdInformation.getTotalSpending()+"/"+thresholdInformation.getThreshold());
        }
    }

    private void getCostForCustomer(String uuid) {
        try {
            CustomerTotalUsageDTO dto = customerService.getUsageForCustomer(UUID.fromString(uuid));
            String dtoJson = mapper.writeValueAsString(dto);
            sendMessage(uuid, dtoJson);
        } catch (Exception e) {
            sendError(uuid, "Error occurred: "+e.getMessage());
        }
    }

    private void changeCostThreshold(UUID customerUuid, Integer newThreshold) {

        try {
            String message = customerService.changeThreshold(customerUuid, newThreshold);

            sendMessage(customerUuid.toString() ,message);

        } catch(Exception e) {
            sendError(customerUuid.toString(), "Failed with exception: "+ e.getMessage());
        }
    }

    private void sendError(String uuid, String message) {
        try {
            WebSocketSession session = getWebSocketSessionForUuid(uuid);
            CostTrackResponseDTO dto = CostTrackResponseDTO.builder()
                    .status("error")
                    .message(message)
                    .build();
            session.sendMessage(new TextMessage(mapper.writeValueAsString(dto)));
        } catch(Exception ignored) {}

    }
}
