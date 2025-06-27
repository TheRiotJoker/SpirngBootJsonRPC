package de.theriotjoker.mathfactory.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationResponseDTO {
    private Double result;
    private int cost;
    private String operationName;
}
