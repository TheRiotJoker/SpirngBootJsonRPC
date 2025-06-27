package de.theriotjoker.mathfactory.utils;

import de.theriotjoker.mathfactory.model.ops.MathFunction;
import de.theriotjoker.mathfactory.repository.ops.MathFunctionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OperationManager {
    private final MathFunctionRepository mathFunctionRepository;

    public OperationManager(MathFunctionRepository mathFunctionRepository) {
        this.mathFunctionRepository = mathFunctionRepository;
    }

    public OperationExecutor get(OperationEnum name) {
        Optional<MathFunction> saved = mathFunctionRepository.findMathFunctionByFunctionName(name.name());
        if(saved.isEmpty()) {
            throw new IllegalArgumentException("The requested function does not exist.");
        }
        return OperationExecutor.of(saved.get());
    }




    public void changeFunction(String functionName, int newCost, boolean enabled) {
        Optional<MathFunction> saved = mathFunctionRepository.findMathFunctionByFunctionName(functionName);

        if(saved.isEmpty()) {
            throw new IllegalArgumentException("The requested function does not exist.");
        }

        MathFunction function = saved.get();

        function.setCost(newCost);
        function.setEnabled(enabled);

        mathFunctionRepository.save(function);
    }
}
