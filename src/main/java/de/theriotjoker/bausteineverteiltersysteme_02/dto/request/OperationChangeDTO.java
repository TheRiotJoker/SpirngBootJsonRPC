package de.theriotjoker.bausteineverteiltersysteme_02.dto.request;

import lombok.Data;

@Data
public class OperationChangeDTO {
    private int newCost;
    private boolean enabled;
    private String operationName;
}
