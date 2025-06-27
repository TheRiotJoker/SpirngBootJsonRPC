package de.theriotjoker.datamanager.model.cust;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer_threshold_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerThresholdData {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(name = "customer_uuid", nullable = false, unique = true)
    private UUID customerUuid;

    @Column(nullable = false)
    @Builder.Default
    private int threshold = 0;
}
