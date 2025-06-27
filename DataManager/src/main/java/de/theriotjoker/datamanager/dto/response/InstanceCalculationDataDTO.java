package de.theriotjoker.datamanager.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InstanceCalculationDataDTO {
    private UUID uuid;
    private int totalCalculations;
    private int totalCost;
}
