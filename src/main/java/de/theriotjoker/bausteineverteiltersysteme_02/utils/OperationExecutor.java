package de.theriotjoker.bausteineverteiltersysteme_02.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
public class OperationExecutor {

    private final BiFunction<Double, Double, Double> fn;
    @Builder.Default
    private boolean isEnabled = true;
    private int cost;

    public static OperationExecutor of(OperationEnum operationEnum) {

        OperationExecutorBuilder builder = new OperationExecutorBuilder();

        return switch (operationEnum) {
            case ADDITION-> builder.cost(2).fn(Double::sum).build();
            case SUBTRACTION -> builder.cost(3).fn((a, b) -> a - b).build();
            case MULTIPLICATION -> builder.cost(25).fn((a, b) -> a * b).build();
            case DIVISION -> builder.cost(50).fn((a, b) -> a / b).build();
            case FACTORIAL -> builder.cost(100).fn((a, ignored) -> factorial(a)).build();
            case POWER -> builder.cost(1150).fn(Math::pow).build();
        };
    }

    private static Double factorial(Double a) {
        if(Double.valueOf(0).equals(a)) {
            return 1.0d;
        }
        Double result = a;
        for(int i = a.intValue()-1; i > 1; i--) {
            result = result * i;
        }
        return result;
    }
}
