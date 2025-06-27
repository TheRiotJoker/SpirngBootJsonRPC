package de.theriotjoker.datamanager.dto.response;

import de.theriotjoker.datamanager.utils.OperationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FunctionUsageData {
    private OperationEnum functionName;
    private Integer totalCost;
    private Integer totalUsage;
}
