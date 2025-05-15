package de.theriotjoker.bausteineverteiltersysteme_02.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CostTrackResponseDTO {
    private String status;
    private String message;
}
