package de.theriotjoker.bausteineverteiltersysteme_02.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CostTracker {

    private Integer spendingThreshold = 0;
    private Integer amountSpent = 0;


    public boolean isThresholdExceeded() {
        if(spendingThreshold == null) {
            return false;
        }
        return amountSpent >= spendingThreshold;
    }

    public void recordSpending(Integer price) {
        amountSpent = amountSpent + price;
    }
}
