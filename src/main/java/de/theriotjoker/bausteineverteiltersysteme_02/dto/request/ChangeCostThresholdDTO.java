package de.theriotjoker.bausteineverteiltersysteme_02.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ChangeCostThresholdDTO {

    private String uuid;

    private Integer newThreshold;
}
