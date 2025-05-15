package de.theriotjoker.bausteineverteiltersysteme_02.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationResponseDTO {
    private Double result;
    private int cost;
    private String operationName;
}
