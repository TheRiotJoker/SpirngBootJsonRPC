package de.theriotjoker.datamanager.service;

import de.theriotjoker.datamanager.dto.response.InstanceCalculationDataDTO;
import de.theriotjoker.datamanager.model.ops.InstanceCalculationData;
import de.theriotjoker.datamanager.repository.ops.InstanceCalculationDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MathInstanceService {
    private InstanceCalculationDataRepository repository;

    public List<InstanceCalculationDataDTO> getAll(List<String> uuids) {
        List<InstanceCalculationDataDTO> allData = new ArrayList<>();
        for(String uuid : uuids) {
            List<InstanceCalculationData> instanceData = repository.getInstanceCalculationDataByInstanceUuid(UUID.fromString(uuid));
            if(!instanceData.isEmpty()) {
                allData.add(mapToDto(instanceData));
            }
        }
        return allData;
    }

    private InstanceCalculationDataDTO mapToDto(List<InstanceCalculationData> list) {
        int totalCalculations = list.stream().mapToInt(InstanceCalculationData::getCounter).sum();
        int totalCost = list.stream().mapToInt(InstanceCalculationData::getTotalCost).sum();

        return InstanceCalculationDataDTO.builder().uuid(list.get(0).getInstanceUuid())
                .totalCalculations(totalCalculations)
                .totalCost(totalCost)
                .build();
    }
}
