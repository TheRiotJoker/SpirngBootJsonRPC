package de.theriotjoker.datamanager.repository.cust;

import de.theriotjoker.datamanager.model.cust.CustomerUsageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerDataRepository extends JpaRepository<CustomerUsageData, UUID> {
    List<CustomerUsageData> getByCustomerUuid(UUID uuid);
}
