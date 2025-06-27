package de.theriotjoker.datamanager.service;

import de.theriotjoker.datamanager.dto.response.CustomerTotalUsageDTO;
import de.theriotjoker.datamanager.dto.response.FunctionUsageData;
import de.theriotjoker.datamanager.dto.response.ThresholdInformationDTO;
import de.theriotjoker.datamanager.model.cust.CustomerThresholdData;
import de.theriotjoker.datamanager.repository.cust.CustomerDataRepository;
import de.theriotjoker.datamanager.repository.cust.CustomerThresholdDataRepository;
import de.theriotjoker.datamanager.model.cust.CustomerUsageData;
import de.theriotjoker.datamanager.utils.OperationEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerThresholdDataRepository thresholdRepository;
    private final CustomerDataRepository spendingRepository;

    @Transactional("custTransactionManager")
    public String changeThreshold(UUID customerUuid, Integer threshold) {

        Optional<CustomerThresholdData> thresholdDataOptional = thresholdRepository.getByCustomerUuid(customerUuid);

        CustomerThresholdData thresholdData = thresholdDataOptional.orElseGet(() ->
                CustomerThresholdData.builder()
                        .customerUuid(customerUuid)
                        .build()
        );


        thresholdData.setThreshold(threshold);

        thresholdRepository.save(thresholdData);

        return "Success, new threshold is: "+thresholdData.getThreshold();
    }

    public CustomerTotalUsageDTO getUsageForCustomer(UUID customerUuid) {
        List<CustomerUsageData> usageData = spendingRepository.getByCustomerUuid(customerUuid);

        if(usageData == null || usageData.isEmpty()) {
            throw new IllegalArgumentException("Customer doesn't exist!");
        }

        List<FunctionUsageData> functionUsageData = usageData.stream().map(data ->
                FunctionUsageData.builder().totalUsage(data.getCounter())
                        .functionName(OperationEnum.valueOf(data.getFunctionName()))
                        .totalCost(data.getTotalCost())
                        .build()).toList();

        Integer totalCost = functionUsageData.stream()
                .mapToInt(FunctionUsageData::getTotalCost)
                .sum();

        Optional<CustomerThresholdData> thresholdDataOptional = thresholdRepository.getByCustomerUuid(customerUuid);

        Integer threshold = thresholdDataOptional.orElseGet(() -> CustomerThresholdData.builder()
                .customerUuid(customerUuid)
                .threshold(0)
                .build())
                .getThreshold();

        return CustomerTotalUsageDTO.builder()
                .usageData(functionUsageData)
                .totalCost(totalCost)
                .customerUuid(customerUuid)
                .threshold(threshold)
                .build();

    }

    public ThresholdInformationDTO getThresholdInformation(UUID customerUuid) {
        Optional<CustomerThresholdData> thresholdDataOptional = thresholdRepository.getByCustomerUuid(customerUuid);

        int threshold = 0;
        if(thresholdDataOptional.isPresent()) {
            threshold = thresholdDataOptional.get().getThreshold();
        }


        List<CustomerUsageData> usageData = spendingRepository.getByCustomerUuid(customerUuid);

        int totalUsage = usageData.stream().mapToInt(CustomerUsageData::getTotalCost).sum();

        return ThresholdInformationDTO.builder().totalSpending(totalUsage).threshold(threshold).build();

    }


}
