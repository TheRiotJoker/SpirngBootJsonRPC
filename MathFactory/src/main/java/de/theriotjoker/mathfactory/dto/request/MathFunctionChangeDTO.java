package de.theriotjoker.mathfactory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MathFunctionChangeDTO {
    private String functionName;
    private Integer newCost;
    private boolean enabled;
}
