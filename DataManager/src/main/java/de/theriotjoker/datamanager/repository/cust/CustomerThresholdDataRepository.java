package de.theriotjoker.datamanager.repository.cust;

import de.theriotjoker.datamanager.model.cust.CustomerThresholdData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerThresholdDataRepository extends JpaRepository<CustomerThresholdData, UUID> {
    Optional<CustomerThresholdData> getByCustomerUuid(UUID uuid);
}
