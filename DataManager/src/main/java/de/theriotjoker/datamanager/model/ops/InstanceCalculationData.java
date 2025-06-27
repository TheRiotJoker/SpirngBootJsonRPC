package de.theriotjoker.datamanager.model.ops;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "instance_calculation_data",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"instance_uuid", "function_name"})
        }
)
public class InstanceCalculationData {
    @Id
    @GeneratedValue
    @Column(name = "uuid")
    private UUID id;
    @Column(name = "instance_uuid")
    private UUID instanceUuid;

    @Column(name = "function_name")
    private String functionName;

    private Integer counter;

    @Column(name = "total_cost")
    private Integer totalCost;

    public void registerCalculation(Integer addition) {
        totalCost = totalCost + addition;
        counter++;
    }
}
