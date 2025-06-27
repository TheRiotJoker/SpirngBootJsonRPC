package de.theriotjoker.mathfactory.model.ops;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "available_functions")
public class MathFunction {
    @Id
    private UUID id;

    @Column(name = "function_name", unique = true, nullable = false)
    private String functionName;

    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private boolean enabled;
}
