package de.theriotjoker.mathfactory.service;

import de.theriotjoker.mathfactory.InstanceId;
import de.theriotjoker.mathfactory.dto.request.MathFunctionChangeDTO;
import de.theriotjoker.mathfactory.dto.response.OperationResponseDTO;
import de.theriotjoker.mathfactory.exception.InvalidParametersException;
import de.theriotjoker.mathfactory.exception.MethodUnavailableException;
import de.theriotjoker.mathfactory.model.ops.InstanceCalculationData;
import de.theriotjoker.mathfactory.repository.ops.InstanceCalculationDataRepository;
import de.theriotjoker.mathfactory.utils.OperationEnum;
import de.theriotjoker.mathfactory.utils.OperationExecutor;
import de.theriotjoker.mathfactory.utils.OperationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OperationService {

    private final OperationManager operationManager;
    private final InstanceCalculationDataRepository opsTrackerRepository;

    private CustomerService customerService;

    private InstanceId instanceId;

    public OperationService(OperationManager manager, InstanceCalculationDataRepository repository, InstanceId instanceBean, CustomerService customerService) {
        opsTrackerRepository = repository;
        operationManager = manager;
        instanceId = instanceBean;
        this.customerService = customerService;
    }

    @Transactional("opsTransactionManager")
    public void changeOperation(MathFunctionChangeDTO dto) {
        operationManager.changeFunction(dto.getFunctionName(), dto.getNewCost(), dto.isEnabled());
    }

    @Transactional("opsTransactionManager")
    public OperationResponseDTO executeOperation(OperationEnum operationName, List<Double> params, String customerUuid) throws Exception {
        OperationExecutor executor = operationManager.get(operationName);

        if(operationName != OperationEnum.FACTORIAL && params.size() <= 1) {
            throw new InvalidParametersException("Invalid amount of parameters provided for the requested function.");
        }

        if(!executor.isEnabled()) {
            throw new MethodUnavailableException("The chosen method is disabled, please try again later.");
        }

        Double a = params.getFirst();
        Double b = params.size() > 1 ? params.get(1) : 0.0d;


        Optional<InstanceCalculationData> calculationDataOptional = opsTrackerRepository
                .get(instanceId.getUuid(), operationName.name());

        InstanceCalculationData calculationData;
        calculationData = calculationDataOptional.orElseGet(() -> InstanceCalculationData.builder().
                instanceUuid(instanceId.getUuid())
                .functionName(operationName.name())
                .counter(0)
                .totalCost(0)
                .build());

        calculationData.registerCalculation(executor.getCost());

        opsTrackerRepository.save(calculationData);

        customerService.recordCustomerRequest(customerUuid, executor);

        Double result =  executor.getFn().apply(a,b);
        return OperationResponseDTO.builder()
                .cost(executor.getCost())
                .operationName(operationName.name())
                .result(result)
                .build();
    }
}
