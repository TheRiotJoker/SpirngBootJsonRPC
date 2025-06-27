package de.theriotjoker.datamanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerTotalUsageDTO {
    private UUID customerUuid;
    private Integer totalCost;
    private Integer threshold;
    private List<FunctionUsageData> usageData;

}
