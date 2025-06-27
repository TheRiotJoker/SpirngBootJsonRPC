package de.theriotjoker.mathfactory.repository.ops;

import de.theriotjoker.mathfactory.model.ops.MathFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MathFunctionRepository extends JpaRepository<MathFunction, UUID> {
    Optional<MathFunction> findMathFunctionByFunctionName(String functionName);
}
