package de.theriotjoker.mathfactory.repository.cust;

import de.theriotjoker.mathfactory.model.cust.CustomerUsageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerUsageDataRepository extends JpaRepository<CustomerUsageData, UUID> {

    List<CustomerUsageData> findByCustomerUuid(UUID uuid);

    Optional<CustomerUsageData> findByCustomerUuidAndFunctionName(UUID uuid, String functionName);


}
