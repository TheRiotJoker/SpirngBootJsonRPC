package de.theriotjoker.datamanager.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CostTrackResponseDTO {
    private String status;
    private String message;
}
