package de.theriotjoker.mathfactory;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InstanceId {
    private final UUID uuid = UUID.randomUUID();

    @Bean
    public UUID getUuid() {
        return uuid;
    }
}
