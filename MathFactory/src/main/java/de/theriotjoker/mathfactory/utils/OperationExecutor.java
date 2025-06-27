package de.theriotjoker.mathfactory.utils;

import de.theriotjoker.mathfactory.model.ops.MathFunction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiFunction;

@Getter
@Setter
@Builder
public class OperationExecutor {

    private final BiFunction<Double, Double, Double> fn;
    private boolean isEnabled;
    private int cost;
    private OperationEnum name;

    public static OperationExecutor of(MathFunction mathFunction) {

        OperationExecutorBuilder builder = new OperationExecutorBuilder();

        OperationEnum operationName = OperationEnum.fromString(mathFunction.getFunctionName());

        builder.name(operationName);
        builder.isEnabled(mathFunction.isEnabled());
        builder.cost(mathFunction.getCost());

        return switch (operationName) {
            case ADDITION-> builder.fn(Double::sum).build();
            case SUBTRACTION -> builder.fn((a, b) -> a - b).build();
            case MULTIPLICATION -> builder.fn((a, b) -> a * b).build();
            case DIVISION -> builder.fn((a, b) -> a / b).build();
            case FACTORIAL -> builder.fn((a, ignored) -> factorial(a)).build();
            case POWER -> builder.fn(Math::pow).build();
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
