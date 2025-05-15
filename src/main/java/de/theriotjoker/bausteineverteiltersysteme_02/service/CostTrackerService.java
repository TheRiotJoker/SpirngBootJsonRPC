package de.theriotjoker.bausteineverteiltersysteme_02.service;

import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.CostTrackResponseDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.utils.CostTracker;
import de.theriotjoker.bausteineverteiltersysteme_02.websocket.CostTrackerWebSocketHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CostTrackerService {
    private final Map<String, CostTracker> costTrackerMap = new HashMap<>();

    public void recordOperation(String uuid, Integer operationCost) {
        costTrackerMap.putIfAbsent(uuid, new CostTracker());

        if(costTrackerMap.get(uuid).isThresholdExceeded()) {
            Integer spent = costTrackerMap.get(uuid).getAmountSpent();
            Integer threshold = costTrackerMap.get(uuid).getSpendingThreshold();
            CostTrackResponseDTO dto = CostTrackResponseDTO.builder()
                    .status("THRESHOLD_EXCEEDED")
                    .message("Threshold exceeded. You have spent: "+spent+"/"+threshold)
                    .build();
            CostTrackerWebSocketHandler.sendMessageTo(uuid, dto);
        }
        costTrackerMap.get(uuid).recordSpending(operationCost);
    }

    public Integer updateThreshold(String uuid, Integer newThreshold) {
        costTrackerMap.putIfAbsent(uuid, new CostTracker());
        costTrackerMap.get(uuid).setSpendingThreshold(newThreshold);

        return costTrackerMap.get(uuid).getSpendingThreshold();
    }
}
