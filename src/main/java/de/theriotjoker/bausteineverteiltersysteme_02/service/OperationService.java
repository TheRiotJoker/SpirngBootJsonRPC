package de.theriotjoker.bausteineverteiltersysteme_02.service;

import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.OperationResponseDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.exception.InvalidParametersException;
import de.theriotjoker.bausteineverteiltersysteme_02.exception.MethodUnavailableException;
import de.theriotjoker.bausteineverteiltersysteme_02.utils.OperationEnum;
import de.theriotjoker.bausteineverteiltersysteme_02.utils.OperationExecutor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.theriotjoker.bausteineverteiltersysteme_02.utils.OperationEnum.FACTORIAL;

@Service
public class OperationService {

    private final CostTrackerService costTrackerService;

    private final Map<OperationEnum, OperationExecutor> operationMap =
            Arrays.stream(OperationEnum.values())
                    .collect(Collectors.toMap(opEnum -> opEnum, OperationExecutor::of));

    public OperationService(CostTrackerService costTrackerService) {
        this.costTrackerService = costTrackerService;
    }

    public OperationResponseDTO executeOperationTracked(OperationEnum operationName, List<Double> params, String uuid) {
        Integer operationCost = operationMap.get(operationName).getCost();



        costTrackerService.recordOperation(uuid, operationCost);


        return executeOperation(operationName, params);
    }

    public OperationResponseDTO executeOperation(OperationEnum operationName, List<Double> params) {
        OperationExecutor executor = operationMap.get(operationName);

        if(operationName != FACTORIAL && params.size() <= 1) {
            throw new InvalidParametersException("Invalid amount of parameters provided for the requested function.");
        }

        Double a = params.getFirst();
        Double b = params.size() > 1 ? params.get(1) : 0.0d;


        if(!executor.isEnabled()) {
            throw new MethodUnavailableException("The chosen method is disabled, please try again later.");
        }

        Double result =  executor.getFn().apply(a,b);
        return OperationResponseDTO.builder()
                .cost(executor.getCost())
                .operationName(operationName.name())
                .result(result)
                .build();
    }

    public void changeOperation(OperationEnum operationName, boolean enabled, int newCost) {
        OperationExecutor executor = operationMap.get(operationName);
        executor.setEnabled(enabled);
        executor.setCost(newCost);
    }
}
