package de.theriotjoker.mathfactory.repository.ops;


import de.theriotjoker.mathfactory.model.ops.InstanceCalculationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstanceCalculationDataRepository extends JpaRepository<InstanceCalculationData, UUID> {

    @Query("SELECT icd FROM InstanceCalculationData icd WHERE icd.instanceUuid = :uuid AND icd.functionName = :name")

    Optional<InstanceCalculationData> get(UUID uuid, String name);
    List<InstanceCalculationData> getInstanceCalculationDataByInstanceUuid(UUID uuid);
}
