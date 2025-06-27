package de.theriotjoker.datamanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ThresholdInformationDTO {
    private Integer totalSpending;
    private Integer threshold;

    public boolean isExceeded() {
        return totalSpending > threshold;
    }
}
