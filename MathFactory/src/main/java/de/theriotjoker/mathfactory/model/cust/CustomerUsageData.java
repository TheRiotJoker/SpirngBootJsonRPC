package de.theriotjoker.mathfactory.model.cust;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "customer_usage_data",
        uniqueConstraints = @UniqueConstraint(columnNames = {"customer_uuid", "function_name"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUsageData {
    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(nullable = false, name = "customer_uuid")
    private UUID customerUuid;

    @Column(nullable = false, length = 50, name="function_name")
    private String functionName;

    @Column(nullable = false)
    private int counter;

    @Column(nullable = false, name = "total_cost")
    private int totalCost;


    public void recordUsage(Integer cost) {
        totalCost = totalCost + cost;
        counter++;
    }
}
