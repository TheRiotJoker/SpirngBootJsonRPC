package de.theriotjoker.mathfactory.service;


import de.theriotjoker.mathfactory.model.cust.CustomerUsageData;
import de.theriotjoker.mathfactory.repository.cust.CustomerUsageDataRepository;
import de.theriotjoker.mathfactory.utils.OperationExecutor;
import de.theriotjoker.mathfactory.utils.WebSocketManager;
import de.theriotjoker.mathfactory.utils.WsMessageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {

    private final CustomerUsageDataRepository customerTrackerRepository;

    private final WebSocketManager webSocketManager;


    @Transactional("custTransactionManager")
    public void recordCustomerRequest(String customerId, OperationExecutor operation) throws Exception {

        UUID customerUuid = UUID.fromString(customerId);
        String functionName = operation.getName().toString();


        Optional<CustomerUsageData> usageDataOptional = customerTrackerRepository.findByCustomerUuidAndFunctionName(customerUuid, functionName);

        CustomerUsageData usageData = usageDataOptional.orElseGet(() -> CustomerUsageData.builder()
                .customerUuid(customerUuid)
                .functionName(functionName)
                .counter(0)
                .totalCost(0)
                .build());

        usageData.recordUsage(operation.getCost());

        webSocketManager.sendMessageToDataManager(WsMessageType.CHECK_THRESHOLD, customerId);

        customerTrackerRepository.save(usageData);
    }
}
